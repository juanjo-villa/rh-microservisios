/// <reference types="vite/client" />

interface ImportMetaEnv {
  readonly MODE: string;
  readonly EMPLOYEE_SERVICE_URL: string;
  readonly PAYROLL_SERVICE_URL: string;
  readonly PERFORMANCE_SERVICE_URL: string;
  readonly SCHEDULE_SERVICE_URL: string;
}

interface ImportMeta {
  readonly env: ImportMetaEnv;
}
