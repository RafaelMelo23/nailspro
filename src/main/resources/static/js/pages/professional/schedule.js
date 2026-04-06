import { CoreModule } from './schedule/core.js';
import { WorkScheduleModule } from './schedule/work-schedule.js';
import { BlocksModule } from './schedule/blocks.js';
import { AgendaModule } from './schedule/agenda.js';

window.professionalScheduleApp = {
    ...CoreModule,
    ...WorkScheduleModule,
    ...BlocksModule,
    ...AgendaModule
};
