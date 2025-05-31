
import React, { useState } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Table, TableHeader, TableBody, TableRow, TableHead, TableCell } from "@/components/ui/table";
import { DollarSign, Download, FileText, Filter, Search, User } from "lucide-react";
import { Input } from "@/components/ui/input";

const PayrollView = () => {
  // Mock data for payroll periods
  const [payrollPeriods] = useState([
    { id: 1, period: "Mayo 2025", startDate: "01/05/2025", endDate: "15/05/2025", status: "Procesado", totalAmount: 187650.00 },
    { id: 2, period: "Abril 2025 (2da quincena)", startDate: "16/04/2025", endDate: "30/04/2025", status: "Procesado", totalAmount: 185420.00 },
    { id: 3, period: "Abril 2025 (1ra quincena)", startDate: "01/04/2025", endDate: "15/04/2025", status: "Cerrado", totalAmount: 184980.00 },
    { id: 4, period: "Marzo 2025 (2da quincena)", startDate: "16/03/2025", endDate: "31/03/2025", status: "Cerrado", totalAmount: 183750.00 },
  ]);

  // Mock data for payroll entries
  const [payrollEntries] = useState([
    { id: 1, employee: "Ana Ramírez", position: "Diseñadora UX", baseSalary: 6500.00, overtime: 450.00, bonus: 200.00, deductions: 980.00, netPay: 6170.00 },
    { id: 2, employee: "Carlos Mendoza", position: "Desarrollador Senior", baseSalary: 8000.00, overtime: 0.00, bonus: 500.00, deductions: 1275.00, netPay: 7225.00 },
    { id: 3, employee: "Laura Torres", position: "Project Manager", baseSalary: 9200.00, overtime: 0.00, bonus: 0.00, deductions: 1380.00, netPay: 7820.00 },
    { id: 4, employee: "Miguel Ángel Díaz", position: "DevOps Engineer", baseSalary: 7800.00, overtime: 585.00, bonus: 0.00, deductions: 1257.75, netPay: 7127.25 },
    { id: 5, employee: "Roberto Sánchez", position: "Analista de Datos", baseSalary: 7200.00, overtime: 270.00, bonus: 0.00, deductions: 1120.50, netPay: 6349.50 },
  ]);

  return (
    <div className="page-container">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold">Gestión de Nómina</h1>
        <div className="flex gap-2">
          <Button variant="outline">
            <Filter className="mr-2 h-4 w-4" />
            Filtros
          </Button>
          <Button>
            <FileText className="mr-2 h-4 w-4" />
            Generar reporte
          </Button>
        </div>
      </div>

      <Card className="mb-6">
        <CardHeader className="pb-2">
          <CardTitle>Períodos de nómina</CardTitle>
          <CardDescription>Seleccione un período para ver detalles</CardDescription>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Período</TableHead>
                <TableHead>Fecha inicio</TableHead>
                <TableHead>Fecha fin</TableHead>
                <TableHead>Estado</TableHead>
                <TableHead>Monto total</TableHead>
                <TableHead>Acciones</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {payrollPeriods.map(period => (
                <TableRow key={period.id}>
                  <TableCell className="font-medium">{period.period}</TableCell>
                  <TableCell>{period.startDate}</TableCell>
                  <TableCell>{period.endDate}</TableCell>
                  <TableCell>
                    <span className={`px-2 py-1 rounded-md text-xs ${
                      period.status === "Procesado" 
                        ? "bg-green-100 text-green-800" 
                        : "bg-blue-100 text-blue-800"
                    }`}>
                      {period.status}
                    </span>
                  </TableCell>
                  <TableCell className="font-medium">${period.totalAmount.toLocaleString('es-MX', {minimumFractionDigits: 2})}</TableCell>
                  <TableCell>
                    <div className="flex space-x-2">
                      <Button variant="ghost" size="sm">
                        <DollarSign size={14} className="mr-1" />
                        Ajustes
                      </Button>
                      <Button variant="outline" size="sm">
                        <Download size={14} className="mr-1" />
                        Descargar
                      </Button>
                    </div>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      <Card>
        <CardHeader className="pb-2">
          <CardTitle>Detalles de nómina - Mayo 2025</CardTitle>
          <div className="flex justify-between items-center mt-2">
            <CardDescription>Mostrando 5 de 127 empleados</CardDescription>
            <div className="flex w-full max-w-sm items-center space-x-2">
              <Input 
                placeholder="Buscar empleado..." 
                className="h-9" 
                type="search"
              />
              <Button size="sm" className="h-9">
                <Search size={16} />
              </Button>
            </div>
          </div>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Empleado</TableHead>
                <TableHead>Salario base</TableHead>
                <TableHead>Horas extra</TableHead>
                <TableHead>Bonos</TableHead>
                <TableHead>Deducciones</TableHead>
                <TableHead>Pago neto</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {payrollEntries.map(entry => (
                <TableRow key={entry.id}>
                  <TableCell>
                    <div>
                      <div className="font-medium flex items-center">
                        <User size={16} className="mr-2 text-gray-500" />
                        {entry.employee}
                      </div>
                      <div className="text-xs text-muted-foreground">{entry.position}</div>
                    </div>
                  </TableCell>
                  <TableCell>${entry.baseSalary.toLocaleString('es-MX', {minimumFractionDigits: 2})}</TableCell>
                  <TableCell>${entry.overtime.toLocaleString('es-MX', {minimumFractionDigits: 2})}</TableCell>
                  <TableCell>${entry.bonus.toLocaleString('es-MX', {minimumFractionDigits: 2})}</TableCell>
                  <TableCell className="text-red-600">-${entry.deductions.toLocaleString('es-MX', {minimumFractionDigits: 2})}</TableCell>
                  <TableCell className="font-bold">${entry.netPay.toLocaleString('es-MX', {minimumFractionDigits: 2})}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>
    </div>
  );
};

export default PayrollView;
