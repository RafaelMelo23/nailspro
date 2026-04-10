export const AppointmentsModule = {
    loadAppointmentsOverview: async function(page = 0) {
        const list = document.getElementById('appointments-overview-list');
        const profSelect = document.getElementById('filter-appt-professional');
        const statusSelect = document.getElementById('filter-appt-status');
        const dateInput = document.getElementById('filter-appt-date');

        if (!list) return;

        const professionalId = profSelect ? profSelect.value : '';
        const status = statusSelect ? statusSelect.value : '';
        const date = dateInput ? dateInput.value : '';

        list.innerHTML = '<tr><td colspan="6" class="empty-state">Carregando agendamentos...</td></tr>';

        try {
            const params = new URLSearchParams({
                professionalId,
                status,
                date,
                page,
                size: 10
            });
            const res = await fetch(`/api/v1/admin/appointments/overview?${params.toString()}`);
            if (res.ok) {
                const data = await res.json();
                this.renderAppointmentsOverview(data.content);
                this.renderAppointmentsPagination(data);
            }
        } catch (error) {
            list.innerHTML = '<tr><td colspan="6" class="empty-state">Erro ao carregar agendamentos.</td></tr>';
        }
    },

    renderAppointmentsOverview: function(appointments) {
        const list = document.getElementById('appointments-overview-list');
        if (!list) return;

        if (appointments.length === 0) {
            list.innerHTML = '<tr><td colspan="6" class="empty-state">Nenhum agendamento encontrado para os filtros selecionados.</td></tr>';
            return;
        }

        const statusMap = {
            'PENDING': { label: 'Pendente', class: 'badge-pending' },
            'CONFIRMED': { label: 'Confirmado', class: 'badge-success' },
            'FINISHED': { label: 'Finalizado', class: 'badge-success' },
            'CANCELLED': { label: 'Cancelado', class: 'badge-danger' },
            'MISSED': { label: 'Faltou', class: 'badge-danger' }
        };

        const formatDate = (dateStr) => {
            const date = new Date(dateStr);
            return date.toLocaleDateString('pt-BR', { day: '2-digit', month: '2-digit', year: 'numeric', hour: '2-digit', minute: '2-digit' });
        };

        list.innerHTML = appointments.map(a => {
            const s = statusMap[a.status] || { label: a.status, class: '' };
            return `
                <tr>
                    <td>${formatDate(a.startDateAndTime)}</td>
                    <td>${a.clientName}</td>
                    <td>${a.professionalName}</td>
                    <td>${a.mainServiceName}</td>
                    <td>R$ ${a.totalValue.toFixed(2).replace('.', ',')}</td>
                    <td><span class="badge ${s.class}">${s.label}</span></td>
                </tr>
            `;
        }).join('');
    },

    renderAppointmentsPagination: function(data) {
        const container = document.getElementById('appointments-overview-pagination');
        if (!container) return;

        if (data.totalPages <= 1) {
            container.innerHTML = '';
            return;
        }

        container.innerHTML = `
            <button class="page-btn" ${data.first ? 'disabled' : ''} onclick="adminSettingsApp.loadAppointmentsOverview(${data.number - 1})">Anterior</button>
            <span style="font-size: 0.85rem; color: #666;">Página ${data.number + 1} de ${data.totalPages}</span>
            <button class="page-btn" ${data.last ? 'disabled' : ''} onclick="adminSettingsApp.loadAppointmentsOverview(${data.number + 1})">Próxima</button>
        `;
    },

    populateProfessionalsFilter: async function() {
        const select = document.getElementById('filter-appt-professional');
        if (!select) return;

        try {
            const res = await fetch('/api/v1/admin/professional');
            if (res.ok) {
                const professionals = await res.json();
                const currentVal = select.value;
                select.innerHTML = '<option value="">Todos os Profissionais</option>' + 
                    professionals.map(p => `<option value="${p.id}">${p.name}</option>`).join('');
                select.value = currentVal;
            }
        } catch (e) {}
    }
};
