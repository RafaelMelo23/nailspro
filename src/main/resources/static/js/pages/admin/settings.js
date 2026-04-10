import { CoreModule } from './settings/settings-core.js';
import { ProfessionalsModule } from './settings/professionals.js';
import { ClientsModule } from './settings/clients.js';
import { SalonModule } from './settings/salon.js';
import { InsightsModule } from './settings/insights.js';
import { AppointmentsModule } from './settings/appointments.js';

window.adminSettingsApp = {
    ...CoreModule,
    ...ProfessionalsModule,
    ...ClientsModule,
    ...SalonModule,
    ...InsightsModule,
    ...AppointmentsModule
};