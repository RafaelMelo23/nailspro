window.NotificationService = {
    eventSource: null,
    whatsappStatus: 'CLOSE',
    connectionTimeout: null,
    connectionPromise: null,
    connectionResolve: null,

    init: function() {
        console.log("Initializing NotificationService...");
        if (!Auth.getToken()) {
            console.log("No auth token found, skipping NotificationService init");
            return;
        }

        if (Auth.hasRole('ADMIN') || Auth.hasRole('PROFESSIONAL')) {
            console.log("User is ADMIN or PROFESSIONAL, subscribing to notifications...");
            this.subscribe();
        } else {
            console.log("User is not ADMIN or PROFESSIONAL, role:", Auth.getPayload()?.roles);
        }
    },

    subscribe: async function() {
        if (this.eventSource) {
            console.log("Closing existing EventSource");
            this.eventSource.close();
        }

        this.connectionPromise = new Promise(resolve => {
            this.connectionResolve = resolve;
        });

        if (Auth.isTokenExpired()) {
            await Auth.refreshToken();
        }

        const token = Auth.getToken();
        if (!token) return;

        const url = `/api/v1/notifications/subscribe?token=${token}`;
        console.log("Connecting to SSE at:", url);
        this.eventSource = new EventSource(url);

        this.eventSource.onopen = () => {
            console.log("SSE Connection opened successfully");
            if (this.connectionResolve) {
                this.connectionResolve();
                this.connectionResolve = null;
            }
        };

        this.eventSource.onmessage = (event) => {
            console.log("SSE Message received:", event.data);
            try {
                const payload = JSON.parse(event.data);
                this.handleNotification(payload);
            } catch (e) {

                console.log("SSE plain text or invalid JSON:", event.data);
            }
        };

        this.eventSource.onerror = (err) => {
            console.error("SSE Connection Error", err);
            this.eventSource.close();
            this.connectionPromise = null;
            this.connectionResolve = null;

            if (Auth.getToken()) {
                setTimeout(() => this.subscribe(), 5000);
            }
        };
    },

    ensureConnected: async function() {
        if (!this.eventSource || this.eventSource.readyState === EventSource.CLOSED) {
            await this.subscribe();
        }
        
        if (this.eventSource.readyState === EventSource.CONNECTING) {
            console.log("SSE is connecting, waiting for open...");

            const timeoutPromise = new Promise((_, reject) => setTimeout(() => reject(new Error('SSE connection timeout')), 5000));
            await Promise.race([this.connectionPromise, timeoutPromise]);
        }
    },

    handleNotification: function(payload) {
        const { sseEventType, data } = payload;

        switch (sseEventType) {
            case 'QR_CODE_UPDATE':
                this.handleQrCodeUpdate(data);
                break;
            case 'CONNECTION_UPDATE':
                this.handleConnectionUpdate(data);
                break;
            default:
                console.log("Unhandled SSE event type:", sseEventType);
        }
    },

    handleQrCodeUpdate: function(data) {
        const qrContainer = document.getElementById('whatsapp-qr-container');
        const pairingContainer = document.getElementById('whatsapp-pairing-container');
        const loading = document.getElementById('whatsapp-loading');
        const retry = document.getElementById('whatsapp-retry');
        const alert = document.getElementById('whatsapp-disconnected-alert');
        const instructions = document.getElementById('whatsapp-instructions');
        
        if (!qrContainer) return;

        this.clearConnectionTimeout();

        if (loading) loading.classList.add('hidden');
        if (retry) retry.classList.remove('hidden');
        if (alert) alert.classList.add('hidden');
        if (instructions) instructions.classList.add('hidden');
        
        if (data.pairingCode) {
            this.updatePairingCodeUI(data.pairingCode);
        } else if (data.code || data.base64 || data.qrcode) {
            const qrSource = data.base64 || data.qrcode || data.code;
            const imgSrc = qrSource.startsWith('data:image') ? qrSource : `data:image/png;base64,${qrSource}`;
            
            qrContainer.innerHTML = `<img src="${imgSrc}" alt="WhatsApp QR Code">`;
            qrContainer.classList.remove('hidden');
            if (pairingContainer) pairingContainer.classList.add('hidden');
            this.showWhatsappPopup();
        }
    },

    handleConnectionUpdate: function(data) {

        if (data && typeof data === 'object' && data.pairingCode) {
            this.updatePairingCodeUI(data.pairingCode);
        }

        const state = typeof data === 'string' ? data : (data.state || data.status);
        if (!state) return;

        this.clearConnectionTimeout();

        this.whatsappStatus = state;
        this.updateStatusUI(state);

        if (state === 'OPEN') {
            this.showSuccessState();
        } else if (state === 'CLOSE' || state === 'DISCONNECTED') {
            this.showWhatsappPopup(true);
        }
    },

    showSuccessState: function() {
        const loading = document.getElementById('whatsapp-loading');
        const qrContainer = document.getElementById('whatsapp-qr-container');
        const pairingContainer = document.getElementById('whatsapp-pairing-container');
        const success = document.getElementById('whatsapp-success');
        const retry = document.getElementById('whatsapp-retry');
        const instructions = document.getElementById('whatsapp-instructions');
        const alert = document.getElementById('whatsapp-disconnected-alert');

        if (loading) loading.classList.add('hidden');
        if (qrContainer) qrContainer.classList.add('hidden');
        if (pairingContainer) pairingContainer.classList.add('hidden');
        if (retry) retry.classList.add('hidden');
        if (instructions) instructions.classList.add('hidden');
        if (alert) alert.classList.add('hidden');
        
        if (success) {
            success.classList.remove('hidden');

            setTimeout(() => {
                this.hideWhatsappPopup();
            }, 3000);
        } else {

            Toast.success("WhatsApp conectado com sucesso!");
            this.hideWhatsappPopup();
        }
    },

    updatePairingCodeUI: function(pairingCode) {
        const codeEl = document.getElementById('whatsapp-pairing-code');
        const pairingContainer = document.getElementById('whatsapp-pairing-container');
        const qrContainer = document.getElementById('whatsapp-qr-container');
        const loading = document.getElementById('whatsapp-loading');
        const alert = document.getElementById('whatsapp-disconnected-alert');
        const instructions = document.getElementById('whatsapp-instructions');
        const success = document.getElementById('whatsapp-success');

        this.clearConnectionTimeout();

        if (codeEl) {

            let formattedCode = pairingCode;
            if (pairingCode && pairingCode.length === 8) {
                formattedCode = pairingCode.substring(0, 4) + '-' + pairingCode.substring(4);
            }

            codeEl.innerText = formattedCode;
            if (pairingContainer) pairingContainer.classList.remove('hidden');
            if (qrContainer) qrContainer.classList.add('hidden');
            if (loading) loading.classList.add('hidden');
            if (alert) alert.classList.add('hidden');
            if (instructions) instructions.classList.add('hidden');
            if (success) success.classList.add('hidden');
            this.showWhatsappPopup();
        }
    },

    updateStatusUI: function(state) {
        const statusEls = document.querySelectorAll('#whatsapp-connection-status');
        statusEls.forEach(el => {
            const dot = el.querySelector('.status-dot');
            const text = el.querySelector('.status-text');
            
            if (!dot || !text) return;
            
            dot.className = 'status-dot';
            
            if (state === 'OPEN') {
                dot.classList.add('status-open');
                text.innerText = 'Conectado';
            } else {
                dot.classList.add('status-close');
                text.innerText = 'Desconectado';
            }
        });
    },

    showWhatsappPopup: function(isDisconnected = false) {
        const popup = document.getElementById('whatsapp-popup');
        const alert = document.getElementById('whatsapp-disconnected-alert');
        const title = document.querySelector('#whatsapp-popup .whatsapp-popup-header h3');

        if (popup) {
            popup.classList.remove('hidden');
            if (isDisconnected) {
                popup.classList.add('disconnected');
                if (alert) alert.classList.remove('hidden');
                if (title) title.innerText = 'WhatsApp Desconectado';
            } else {
                popup.classList.remove('disconnected');

                const qrHidden = document.getElementById('whatsapp-qr-container')?.classList.contains('hidden');
                const pairingHidden = document.getElementById('whatsapp-pairing-container')?.classList.contains('hidden');
                
                if (qrHidden && pairingHidden) {
                    if (alert) alert.classList.add('hidden');
                    if (title) title.innerText = 'Conectar WhatsApp';
                }
            }
        }
    },

    hideWhatsappPopup: function() {
        const popup = document.getElementById('whatsapp-popup');
        if (popup) popup.classList.add('hidden');
        this.resetPopup();
    },

    resetPopup: function() {
        const qrContainer = document.getElementById('whatsapp-qr-container');
        const pairingContainer = document.getElementById('whatsapp-pairing-container');
        const loading = document.getElementById('whatsapp-loading');
        const instructions = document.getElementById('whatsapp-instructions');
        const retry = document.getElementById('whatsapp-retry');
        const alert = document.getElementById('whatsapp-disconnected-alert');
        const success = document.getElementById('whatsapp-success');
        const popup = document.getElementById('whatsapp-popup');
        const title = document.querySelector('#whatsapp-popup .whatsapp-popup-header h3');

        this.clearConnectionTimeout();

        if (qrContainer) qrContainer.classList.add('hidden');
        if (pairingContainer) pairingContainer.classList.add('hidden');
        if (loading) loading.classList.add('hidden');
        if (instructions) instructions.classList.remove('hidden');
        if (retry) retry.classList.add('hidden');
        if (alert) alert.classList.add('hidden');
        if (success) success.classList.add('hidden');
        if (popup) popup.classList.remove('disconnected');
        if (title) title.innerText = 'Conectar WhatsApp';

        if (this.whatsappStatus !== 'OPEN') {
            this.updateStatusUI('CLOSE');
        }
    },

    retryConnection: function() {
        this.resetPopup();
    },

    clearConnectionTimeout: function() {
        if (this.connectionTimeout) {
            clearTimeout(this.connectionTimeout);
            this.connectionTimeout = null;
        }
    },

    startWhatsappConnection: async function(method) {
        const loading = document.getElementById('whatsapp-loading');
        const instructions = document.getElementById('whatsapp-instructions');
        const retry = document.getElementById('whatsapp-retry');
        const qrContainer = document.getElementById('whatsapp-qr-container');
        const pairingContainer = document.getElementById('whatsapp-pairing-container');
        
        if (loading) loading.classList.remove('hidden');
        if (instructions) instructions.classList.add('hidden');
        if (retry) retry.classList.add('hidden');
        if (qrContainer) qrContainer.classList.add('hidden');
        if (pairingContainer) pairingContainer.classList.add('hidden');

        this.clearConnectionTimeout();
        this.connectionTimeout = setTimeout(() => {
            const currentLoading = document.getElementById('whatsapp-loading');
            const currentRetry = document.getElementById('whatsapp-retry');
            if (currentLoading && !currentLoading.classList.contains('hidden')) {
                if (currentRetry) currentRetry.classList.remove('hidden');
            }
        }, 15000); // 15 seconds timeout

        try {

            await this.ensureConnected();

            const res = await fetch(`/api/v1/whatsapp?connectionMethod=${method}`, {
                method: 'POST'
            });

            if (!res.ok) {
                this.clearConnectionTimeout();
                Toast.error("Erro ao iniciar conexão com WhatsApp.");
                if (loading) loading.classList.add('hidden');
                if (instructions) instructions.classList.remove('hidden');
            } else {
                const data = await res.json();

                if (data.pairingCode) {
                    this.updatePairingCodeUI(data.pairingCode);
                }
            }
        } catch (err) {
            this.clearConnectionTimeout();
            console.error("WhatsApp connection error:", err);
            Toast.error("Erro ao conectar ao servidor de notificações.");
            if (loading) loading.classList.add('hidden');
            if (instructions) instructions.classList.remove('hidden');
        }
    }
};
