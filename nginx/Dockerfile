FROM nginx:latest

# Copiar el archivo de configuración de Nginx
COPY nginx.conf /etc/nginx/nginx.conf

# Exponer el puerto 80
EXPOSE 80

# Usar el comando por defecto de Nginx
CMD ["nginx", "-g", "daemon off;"]
