
import React, { useState, useRef } from "react";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Table, TableHeader, TableBody, TableRow, TableHead, TableCell } from "@/components/ui/table";
import { Button } from "@/components/ui/button";
import { Calendar, Download, DollarSign, Clock, FileText, TrendingUp } from "lucide-react";
import { toast } from "@/components/ui/use-toast";
import jsPDF from "jspdf";
import "jspdf-autotable";

const MyPayroll = () => {
  // Mock data for payroll history
  const [payrollHistory] = useState([
    { id: 1, period: "Mayo 2025 (1ra quincena)", date: "15/05/2025", grossPay: 4100.00, deductions: 984.00, netPay: 3116.00 },
    { id: 2, period: "Abril 2025 (2da quincena)", date: "30/04/2025", grossPay: 4100.00, deductions: 984.00, netPay: 3116.00 },
    { id: 3, period: "Abril 2025 (1ra quincena)", date: "15/04/2025", grossPay: 4100.00, deductions: 984.00, netPay: 3116.00 },
    { id: 4, period: "Marzo 2025 (2da quincena)", date: "31/03/2025", grossPay: 4100.00, deductions: 984.00, netPay: 3116.00 },
  ]);

  // Function to generate and download PDF
  const generatePayrollPDF = (payroll) => {
    try {
      // Create a new PDF document
      const doc = new jsPDF();

      // Add company logo or header
      doc.setFontSize(20);
      doc.setTextColor(0, 51, 102);
      doc.text("RH Systems", 105, 20, { align: "center" });

      // Add payroll title
      doc.setFontSize(16);
      doc.setTextColor(0, 0, 0);
      doc.text(`Recibo de Nómina - ${payroll.period}`, 105, 30, { align: "center" });

      // Add date and employee info
      doc.setFontSize(10);
      doc.text(`Fecha de emisión: ${payroll.date}`, 20, 45);
      doc.text(`Empleado: Juan Pérez`, 20, 50);
      doc.text(`Posición: Desarrollador Senior`, 20, 55);
      doc.text(`ID de Empleado: EMP-2023-001`, 20, 60);

      // Add horizontal line
      doc.setDrawColor(220, 220, 220);
      doc.line(20, 65, 190, 65);

      // Add summary section
      doc.setFontSize(12);
      doc.setTextColor(0, 51, 102);
      doc.text("Resumen de Pago", 20, 75);

      // Add summary table
      doc.autoTable({
        startY: 80,
        head: [["Concepto", "Monto"]],
        body: [
          ["Salario Base", `$${currentPay.baseSalary.toLocaleString('es-MX', {minimumFractionDigits: 2})}`],
          ["Horas Extra", `$${currentPay.overtime.toLocaleString('es-MX', {minimumFractionDigits: 2})}`],
          ["Bonos", `$${currentPay.bonus.toLocaleString('es-MX', {minimumFractionDigits: 2})}`],
          ["Pago Bruto", `$${payroll.grossPay.toLocaleString('es-MX', {minimumFractionDigits: 2})}`],
          ["Deducciones", `-$${payroll.deductions.toLocaleString('es-MX', {minimumFractionDigits: 2})}`],
          ["Pago Neto", `$${payroll.netPay.toLocaleString('es-MX', {minimumFractionDigits: 2})}`]
        ],
        theme: 'striped',
        headStyles: { fillColor: [0, 51, 102], textColor: [255, 255, 255] },
        alternateRowStyles: { fillColor: [240, 240, 240] },
        columnStyles: {
          0: { cellWidth: 100 },
          1: { cellWidth: 70, halign: 'right' }
        }
      });

      // Add deductions breakdown
      const finalY = doc.lastAutoTable.finalY + 10;
      doc.setFontSize(12);
      doc.setTextColor(0, 51, 102);
      doc.text("Desglose de Deducciones", 20, finalY);

      doc.autoTable({
        startY: finalY + 5,
        head: [["Concepto", "Monto"]],
        body: [
          ["Impuestos", `$${currentPay.taxes.toLocaleString('es-MX', {minimumFractionDigits: 2})}`],
          ["Seguro Médico", `$${currentPay.insurance.toLocaleString('es-MX', {minimumFractionDigits: 2})}`],
          ["Otros", `$${currentPay.otherDeductions.toLocaleString('es-MX', {minimumFractionDigits: 2})}`],
          ["Total Deducciones", `$${currentPay.totalDeductions.toLocaleString('es-MX', {minimumFractionDigits: 2})}`]
        ],
        theme: 'striped',
        headStyles: { fillColor: [153, 0, 0], textColor: [255, 255, 255] },
        alternateRowStyles: { fillColor: [240, 240, 240] },
        columnStyles: {
          0: { cellWidth: 100 },
          1: { cellWidth: 70, halign: 'right' }
        }
      });

      // Add footer
      const pageCount = doc.internal.getNumberOfPages();
      for (let i = 1; i <= pageCount; i++) {
        doc.setPage(i);
        doc.setFontSize(8);
        doc.setTextColor(100, 100, 100);
        doc.text(
          `Este documento es un comprobante de pago y no requiere firma. Página ${i} de ${pageCount}`,
          105,
          doc.internal.pageSize.height - 10,
          { align: "center" }
        );
      }

      // Save the PDF
      doc.save(`Nomina_${payroll.period.replace(/\s/g, '_')}.pdf`);

      // Show success message
      toast({
        title: "PDF generado con éxito",
        description: `Se ha descargado el recibo de nómina para ${payroll.period}`,
      });
    } catch (error) {
      console.error("Error generating PDF:", error);
      toast({
        title: "Error al generar PDF",
        description: "Ocurrió un error al generar el PDF. Por favor intente nuevamente.",
        variant: "destructive",
      });
    }
  };

  // Mock data for current pay details
  const currentPay = {
    baseSalary: 4000.00,
    overtime: 100.00,
    bonus: 0.00,
    grossPay: 4100.00,
    taxes: 820.00,
    insurance: 123.00,
    otherDeductions: 41.00,
    totalDeductions: 984.00,
    netPay: 3116.00,
    paymentDate: "15/05/2025",
    payPeriod: "01/05/2025 - 15/05/2025",
    hoursWorked: 80
  };

  return (
    <div className="page-container">
      <h1 className="text-2xl font-bold mb-6">Mi Nómina</h1>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-4 mb-6">
        <Card className="bg-green-50">
          <CardContent className="pt-6">
            <div className="flex justify-between items-center">
              <div>
                <p className="text-green-800 font-medium mb-1">Pago neto actual</p>
                <h3 className="text-2xl font-bold">${currentPay.netPay.toLocaleString('es-MX', {minimumFractionDigits: 2})}</h3>
                <p className="text-xs text-muted-foreground mt-1">{currentPay.payPeriod}</p>
              </div>
              <div className="h-12 w-12 bg-green-100 rounded-full flex items-center justify-center">
                <DollarSign className="h-6 w-6 text-green-800" />
              </div>
            </div>
          </CardContent>
        </Card>

        <Card className="bg-blue-50">
          <CardContent className="pt-6">
            <div className="flex justify-between items-center">
              <div>
                <p className="text-blue-800 font-medium mb-1">Horas trabajadas</p>
                <h3 className="text-2xl font-bold">{currentPay.hoursWorked} hrs</h3>
                <p className="text-xs text-muted-foreground mt-1">Este período</p>
              </div>
              <div className="h-12 w-12 bg-blue-100 rounded-full flex items-center justify-center">
                <Clock className="h-6 w-6 text-blue-800" />
              </div>
            </div>
          </CardContent>
        </Card>

        <Card className="bg-purple-50">
          <CardContent className="pt-6">
            <div className="flex justify-between items-center">
              <div>
                <p className="text-purple-800 font-medium mb-1">Próximo pago</p>
                <h3 className="text-2xl font-bold">{currentPay.paymentDate}</h3>
                <p className="text-xs text-muted-foreground mt-1">En 7 días</p>
              </div>
              <div className="h-12 w-12 bg-purple-100 rounded-full flex items-center justify-center">
                <Calendar className="h-6 w-6 text-purple-800" />
              </div>
            </div>
          </CardContent>
        </Card>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
        <div className="md:col-span-2">
          <Card>
            <CardHeader>
              <CardTitle>Historial de pagos</CardTitle>
              <CardDescription>Últimos recibos de nómina</CardDescription>
            </CardHeader>
            <CardContent>
              <Table>
                <TableHeader>
                  <TableRow>
                    <TableHead>Período</TableHead>
                    <TableHead>Fecha</TableHead>
                    <TableHead>Pago bruto</TableHead>
                    <TableHead>Deducciones</TableHead>
                    <TableHead>Pago neto</TableHead>
                    <TableHead></TableHead>
                  </TableRow>
                </TableHeader>
                <TableBody>
                  {payrollHistory.map((item) => (
                    <TableRow key={item.id}>
                      <TableCell className="font-medium">{item.period}</TableCell>
                      <TableCell>{item.date}</TableCell>
                      <TableCell>${item.grossPay.toLocaleString('es-MX', {minimumFractionDigits: 2})}</TableCell>
                      <TableCell className="text-red-600">-${item.deductions.toLocaleString('es-MX', {minimumFractionDigits: 2})}</TableCell>
                      <TableCell className="font-bold">${item.netPay.toLocaleString('es-MX', {minimumFractionDigits: 2})}</TableCell>
                      <TableCell>
                        <Button 
                          variant="ghost" 
                          size="sm"
                          onClick={() => generatePayrollPDF(item)}
                        >
                          <Download size={14} className="mr-1" />
                          PDF
                        </Button>
                      </TableCell>
                    </TableRow>
                  ))}
                </TableBody>
              </Table>
            </CardContent>
          </Card>
        </div>

        <div>
          <Card>
            <CardHeader>
              <CardTitle>Detalles del pago actual</CardTitle>
              <CardDescription>Mayo 2025 (1ra quincena)</CardDescription>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                <div>
                  <h4 className="text-sm font-medium mb-2 flex items-center">
                    <TrendingUp size={16} className="mr-1 text-green-600" />
                    Ingresos
                  </h4>
                  <div className="bg-muted/40 rounded-md p-3 space-y-2">
                    <div className="flex justify-between text-sm">
                      <span>Salario base</span>
                      <span className="font-medium">${currentPay.baseSalary.toLocaleString('es-MX', {minimumFractionDigits: 2})}</span>
                    </div>
                    <div className="flex justify-between text-sm">
                      <span>Horas extra</span>
                      <span className="font-medium">${currentPay.overtime.toLocaleString('es-MX', {minimumFractionDigits: 2})}</span>
                    </div>
                    <div className="flex justify-between font-medium border-t pt-2">
                      <span>Total ingresos</span>
                      <span className="text-green-700">${currentPay.grossPay.toLocaleString('es-MX', {minimumFractionDigits: 2})}</span>
                    </div>
                  </div>
                </div>

                <div>
                  <h4 className="text-sm font-medium mb-2 flex items-center">
                    <FileText size={16} className="mr-1 text-red-600" />
                    Deducciones
                  </h4>
                  <div className="bg-muted/40 rounded-md p-3 space-y-2">
                    <div className="flex justify-between text-sm">
                      <span>Impuestos</span>
                      <span className="font-medium">-${currentPay.taxes.toLocaleString('es-MX', {minimumFractionDigits: 2})}</span>
                    </div>
                    <div className="flex justify-between text-sm">
                      <span>Seguro médico</span>
                      <span className="font-medium">-${currentPay.insurance.toLocaleString('es-MX', {minimumFractionDigits: 2})}</span>
                    </div>
                    <div className="flex justify-between text-sm">
                      <span>Otros</span>
                      <span className="font-medium">-${currentPay.otherDeductions.toLocaleString('es-MX', {minimumFractionDigits: 2})}</span>
                    </div>
                    <div className="flex justify-between font-medium border-t pt-2">
                      <span>Total deducciones</span>
                      <span className="text-red-700">-${currentPay.totalDeductions.toLocaleString('es-MX', {minimumFractionDigits: 2})}</span>
                    </div>
                  </div>
                </div>

                <div className="bg-secondary p-4 rounded-md">
                  <div className="flex justify-between items-center">
                    <div className="font-semibold">
                      <span>Pago neto</span>
                      <span className="text-primary text-lg ml-2">${currentPay.netPay.toLocaleString('es-MX', {minimumFractionDigits: 2})}</span>
                    </div>
                    <Button 
                      onClick={() => generatePayrollPDF({
                        id: 1,
                        period: "Mayo 2025 (1ra quincena)",
                        date: currentPay.paymentDate,
                        grossPay: currentPay.grossPay,
                        deductions: currentPay.totalDeductions,
                        netPay: currentPay.netPay
                      })}
                      className="bg-primary hover:bg-primary/90"
                    >
                      <Download size={16} className="mr-2" />
                      Descargar PDF
                    </Button>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>
      </div>
    </div>
  );
};

export default MyPayroll;
