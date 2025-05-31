import { defineConfig, loadEnv } from "vite";
import react from "@vitejs/plugin-react-swc";
import path from "path";
import { componentTagger } from "lovable-tagger";

// https://vitejs.dev/config/
export default defineConfig(({ mode }) => {
  // Load env file based on mode
  const env = loadEnv(mode, process.cwd(), '');

  return {
    server: {
      host: "::",
      port: 3000,
      strictPort: true, // This forces Vite to use the specified port or fail
      open: true, // Automatically open browser when server starts
    },
    plugins: [
      react(),
      mode === 'development' &&
      componentTagger(),
    ].filter(Boolean),
    resolve: {
      alias: {
        "@": path.resolve(__dirname, "./src"),
      },
    },
    build: {
      rollupOptions: {
        external: []
      }
    },
    // Make env variables available to the app
    define: {
      'import.meta.env.EMPLOYEE_SERVICE_URL': JSON.stringify(env.EMPLOYEE_SERVICE_URL || 'http://localhost:8005'),
      'import.meta.env.PAYROLL_SERVICE_URL': JSON.stringify(env.PAYROLL_SERVICE_URL || 'http://localhost:8006'),
      'import.meta.env.PERFORMANCE_SERVICE_URL': JSON.stringify(env.PERFORMANCE_SERVICE_URL || 'http://localhost:8007'),
      'import.meta.env.SCHEDULE_SERVICE_URL': JSON.stringify(env.SCHEDULE_SERVICE_URL || 'http://localhost:8008'),
    }
  };
});
