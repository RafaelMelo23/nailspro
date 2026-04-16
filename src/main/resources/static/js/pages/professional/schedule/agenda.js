export const AgendaModule = {
    currentDate: new Date(),
    agendaInitialized: false,

    initAgenda: function() {
        if (!this.agendaInitialized) {
            this.bindAgendaEvents();
            this.agendaInitialized = true;
        }
        this.renderDate();
        this.loadAgenda();
    },

    bindAgendaEvents: function() {
        const prevBtn = document.getElementById('prev-day');
        const nextBtn = document.getElementById('next-day');
        const datePicker = document.getElementById('agenda-datepicker');

        if (prevBtn) {
            prevBtn.onclick = () => {
                this.currentDate.setDate(this.currentDate.getDate() - 1);
                this.renderDate();
                this.loadAgenda();
            };
        }

        if (nextBtn) {
            nextBtn.onclick = () => {
                this.currentDate.setDate(this.currentDate.getDate() + 1);
                this.renderDate();
                this.loadAgenda();
            };
        }

        if (datePicker) {
            datePicker.onchange = (e) => {
                const [year, month, day] = e.target.value.split('-');
                this.currentDate = new Date(year, month - 1, day);
                this.renderDate();
                this.loadAgenda();
            };
        }
    },

    renderDate: function() {
        const display = document.getElementById('display-date');
        const picker = document.getElementById('agenda-datepicker');
        if (!display) return;

        const now = new Date();
        const isToday = this.currentDate.toDateString() === now.toDateString();
        
        const options = { weekday: 'long', day: 'numeric', month: 'long' };
        let dateStr = this.currentDate.toLocaleDateString('pt-BR', options);
        
        if (isToday) {
            dateStr = 'Hoje, ' + this.currentDate.toLocaleDateString('pt-BR', { day: 'numeric', month: 'long' });
        }

        display.innerText = dateStr.charAt(0).toUpperCase() + dateStr.slice(1);
        
        if (picker) {
            const y = this.currentDate.getFullYear();
            const m = String(this.currentDate.getMonth() + 1).padStart(2, '0');
            const d = String(this.currentDate.getDate()).padStart(2, '0');
            picker.value = `${y}-${m}-${d}`;
        }
    },

    loadAgenda: async function() {
        const container = document.getElementById('agenda-container');
        if (!container) return;

        this.showSkeletons();

        const start = new Date(this.currentDate);
        start.setHours(0, 0, 0, 0);
        const end = new Date(this.currentDate);
        end.setHours(23, 59, 59, 999);

        try {
            const res = await fetch(`/api/v1/professional/appointments?start=${start.toISOString()}&end=${end.toISOString()}`);
            if (res.ok) {
                const appointments = await res.json();
                this.renderAgenda(appointments);
            }
        } catch (err) {
            container.innerHTML = '<div class="error-placeholder">Erro ao carregar agenda.</div>';
        }
    },

    showSkeletons: function() {
        const container = document.getElementById('agenda-container');
        if (!container) return;
        
        container.innerHTML = `
            <div class="appointment-card skeleton-container" style="pointer-events: none; opacity: 0.6; margin-bottom: 15px; padding: 20px; border: 1px solid var(--border); border-radius: 12px;">
                <div style="display: flex; gap: 15px;">
                    <div class="skeleton skeleton-circle" style="width: 40px; height: 40px;"></div>
                    <div style="flex: 1;">
                        <div class="skeleton skeleton-title" style="width: 150px; height: 20px;"></div>
                        <div class="skeleton skeleton-text" style="width: 100px; height: 15px;"></div>
                    </div>
                </div>
            </div>
        `.repeat(3);
    },

    renderAgenda: function(appointments) {
        const container = document.getElementById('agenda-container');
        const template = document.getElementById('appointment-card-template');
        const summaryCount = document.getElementById('summary-count');
        const summaryRevenue = document.getElementById('summary-revenue');

        if (!container || !template) return;

        container.innerHTML = '';
        
        if (appointments.length === 0) {
            container.innerHTML = '<div class="empty-placeholder">Nenhum agendamento para este dia.</div>';
            if (summaryCount) summaryCount.innerText = '0';
            if (summaryRevenue) summaryRevenue.innerText = 'R$ 0,00';
            return;
        }

        let totalRevenue = 0;
        appointments.sort((a, b) => new Date(a.startDate) - new Date(b.startDate));

        appointments.forEach(ap => {
            const clone = template.content.cloneNode(true);
            const card = clone.querySelector('.appointment-card');
            card.dataset.id = ap.appointmentId;
            card.classList.add(`status-${ap.status.toLowerCase()}`);

            const start = new Date(ap.startDate);
            const end = new Date(ap.endDate);
            const durationMin = Math.round((end - start) / 60000);

            clone.querySelector('.time-start').innerText = start.toLocaleTimeString('pt-BR', { hour: '2-digit', minute: '2-digit' });
            clone.querySelector('.time-duration').innerText = `${durationMin} min`;
            clone.querySelector('.client-name').innerText = ap.clientName;
            clone.querySelector('.service-name').innerText = ap.serviceName || 'Serviço não informado';
            clone.querySelector('.total-price').innerText = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(ap.totalValue);

            const waLink = clone.querySelector('.whatsapp-btn');
            const cleanPhone = ap.clientPhoneNumber.replace(/\D/g, '');
            waLink.href = `https://wa.me/55${cleanPhone}`;

            const statusBadge = clone.querySelector('.status-badge');
            statusBadge.innerText = this.translateStatus(ap.status);
            statusBadge.className = `status-badge status-${ap.status.toLowerCase()}`;

            const warnings = (ap.clientMissedAppointments || 0) + (ap.clientCanceledAppointments || 0);
            if (warnings >= 2) {
                const warningBadge = clone.querySelector('.warning-badge');
                warningBadge.style.display = 'inline-flex';
                clone.querySelector('.warning-count').innerText = warnings;
            }

            if (ap.observations) {
                const obsBtn = clone.querySelector('.btn-toggle-obs');
                const obsContent = clone.querySelector('.obs-content');
                obsBtn.style.display = 'block';
                obsContent.innerText = ap.observations;
                obsBtn.onclick = () => {
                    obsContent.style.display = obsContent.style.display === 'none' ? 'block' : 'none';
                };
            }

            this.renderActions(clone.querySelector('.appointment-actions-column'), ap);

            container.appendChild(clone);
            
            if (ap.status !== 'CANCELLED' && ap.status !== 'MISSED') {
                totalRevenue += ap.totalValue;
            }
        });

        if (summaryCount) summaryCount.innerText = appointments.length;
        if (summaryRevenue) summaryRevenue.innerText = new Intl.NumberFormat('pt-BR', { style: 'currency', currency: 'BRL' }).format(totalRevenue);
    },

    renderActions: function(container, ap) {
        container.innerHTML = '';
        
        if (ap.status === 'PENDING') {
            container.appendChild(this.createActionButton('Confirmar', 'btn-confirm', () => this.updateStatus(ap.appointmentId, 'confirm')));
            container.appendChild(this.createActionButton('Cancelar', 'btn-cancel-link', () => this.updateStatus(ap.appointmentId, 'cancel')));
        } else if (ap.status === 'CONFIRMED') {
            container.appendChild(this.createActionButton('Finalizar', 'btn-finish', () => this.updateStatus(ap.appointmentId, 'finish')));
            container.appendChild(this.createActionButton('Faltou', 'btn-miss', () => this.updateStatus(ap.appointmentId, 'miss')));
            container.appendChild(this.createActionButton('Cancelar', 'btn-cancel-link', () => this.updateStatus(ap.appointmentId, 'cancel')));
        }
    },

    createActionButton: function(text, className, onClick) {
        const btn = document.createElement('button');
        btn.innerText = text;
        btn.className = `btn-action ${className}`;
        btn.onclick = onClick;
        return btn;
    },

    updateStatus: async function(id, action) {
        const msg = {
            'confirm': 'confirmar',
            'finish': 'finalizar',
            'cancel': 'cancelar',
            'miss': 'marcar como falta'
        };
        const confirmed = await UI.confirm('Atualizar Status', `Deseja realmente ${msg[action] || action} este agendamento?`);
        if (!confirmed) return;

        try {
            const res = await fetch(`/api/v1/professional/appointments/${id}/${action}`, { method: 'PATCH' });
            if (res.ok) {
                this.loadAgenda();
                Toast.success('Status atualizado com sucesso.');
            }
        } catch (err) {
            console.error(err);
            Toast.error('Erro ao atualizar status.');
        }
    },

    translateStatus: function(status) {
        const map = {
            'PENDING': 'Pendente',
            'CONFIRMED': 'Confirmado',
            'CANCELLED': 'Cancelado',
            'MISSED': 'Faltou',
            'FINISHED': 'Finalizado'
        };
        return map[status] || status;
    }
};
