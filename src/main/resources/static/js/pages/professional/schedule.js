import { CoreModule } from './schedule/schedule-core.js';
import { WorkScheduleModule } from './schedule/work-schedule.js';
import { BlocksModule } from './schedule/blocks.js';
import { AgendaModule } from './schedule/agenda.js';
import { OverviewModule } from './schedule/overview.js';

window.professionalScheduleApp = {
    ...CoreModule,
    ...WorkScheduleModule,
    ...BlocksModule,
    ...AgendaModule,
    ...OverviewModule
};
