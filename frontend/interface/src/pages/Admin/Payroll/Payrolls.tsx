import React, { useState, useEffect } from "react";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle, CardDescription } from "@/components/ui/card";
import { Table, TableHeader, TableBody, TableRow, TableHead, TableCell } from "@/components/ui/table";
import { Dialog, DialogContent, DialogHeader, DialogTitle, DialogDescription, DialogFooter } from "@/components/ui/dialog";
import { Input } from "@/components/ui/input";
import { Label } from "@/components/ui/label";
import { Select, SelectContent, SelectItem, SelectTrigger, SelectValue } from "@/components/ui/select";
import { DollarSign, Plus, Edit, Trash, Eye } from "lucide-react";
import { toast } from "@/components/ui/use-toast";

// Mock data for employees
const EMPLOYEES = [
  { id: 1, name: "Ana Ramírez", position: "Diseñadora UX", baseSalary: 6500.00 },
  { id: 2, name: "Carlos Mendoza", position: "Desarrollador Senior", baseSalary: 8000.00 },
  { id: 3, name: "Laura Torres", position: "Project Manager", baseSalary: 9200.00 },
  { id: 4, name: "Miguel Ángel Díaz", position: "DevOps Engineer", baseSalary: 7800.00 },
  { id: 5, name: "Roberto Sánchez", position: "Analista de Datos", baseSalary: 7200.00 },
];

// Mock data for payrolls
const INITIAL_PAYROLLS = [
  { 
    id: 1, 
    employeeId: 1, 
    employeeName: "Ana Ramírez", 
    position: "Diseñadora UX", 
    issueDate: "2025-05-15", 
    baseSalary: 6500.00, 
    totalAdjustments: 650.00, 
    netSalary: 7150.00, 
    status: "Paid" 
  },
  { 
    id: 2, 
    employeeId: 2, 
    employeeName: "Carlos Mendoza", 
    position: "Desarrollador Senior", 
    issueDate: "2025-05-15", 
    baseSalary: 8000.00, 
    totalAdjustments: 500.00, 
    netSalary: 8500.00, 
    status: "Paid" 
  },
  { 
    id: 3, 
    employeeId: 3, 
    employeeName: "Laura Torres", 
    position: "Project Manager", 
    issueDate: "2025-05-15", 
    baseSalary: 9200.00, 
    totalAdjustments: -500.00, 
    netSalary: 8700.00, 
    status: "Pending" 
  },
];

// Mock data for adjustments
const INITIAL_ADJUSTMENTS = [
  { id: 1, payrollId: 1, type: "BONUS", amount: 650.00, description: "Performance bonus" },
  { id: 2, payrollId: 2, type: "BONUS", amount: 500.00, description: "Project completion bonus" },
  { id: 3, payrollId: 3, type: "DISCOUNT", amount: 500.00, description: "Advance payment" },
];

const Payrolls = () => {
  const [payrolls, setPayrolls] = useState(INITIAL_PAYROLLS);
  const [adjustments, setAdjustments] = useState(INITIAL_ADJUSTMENTS);
  const [isCreatePayrollOpen, setIsCreatePayrollOpen] = useState(false);
  const [isAdjustmentsOpen, setIsAdjustmentsOpen] = useState(false);
  const [selectedPayroll, setSelectedPayroll] = useState(null);
  const [newPayroll, setNewPayroll] = useState({
    employeeId: "",
    issueDate: new Date().toISOString().split('T')[0],
    status: "Pending"
  });
  const [newAdjustment, setNewAdjustment] = useState({
    type: "BONUS",
    amount: "",
    description: ""
  });
  const [selectedEmployee, setSelectedEmployee] = useState(null);

  // Handle employee selection in the create payroll form
  const handleEmployeeChange = (value) => {
    const employee = EMPLOYEES.find(emp => emp.id.toString() === value);
    setSelectedEmployee(employee);
    setNewPayroll({
      ...newPayroll,
      employeeId: value
    });
  };

  // Create a new payroll
  const handleCreatePayroll = () => {
    if (!newPayroll.employeeId || !selectedEmployee) {
      toast({
        title: "Error",
        description: "Please select an employee",
        variant: "destructive"
      });
      return;
    }

    const newPayrollItem = {
      id: payrolls.length + 1,
      employeeId: parseInt(newPayroll.employeeId),
      employeeName: selectedEmployee.name,
      position: selectedEmployee.position,
      issueDate: newPayroll.issueDate,
      baseSalary: selectedEmployee.baseSalary,
      totalAdjustments: 0,
      netSalary: selectedEmployee.baseSalary,
      status: newPayroll.status
    };

    setPayrolls([...payrolls, newPayrollItem]);
    setIsCreatePayrollOpen(false);
    setNewPayroll({
      employeeId: "",
      issueDate: new Date().toISOString().split('T')[0],
      status: "Pending"
    });
    setSelectedEmployee(null);

    toast({
      title: "Success",
      description: "Payroll created successfully",
    });
  };

  // Open the adjustments modal for a specific payroll
  const handleViewAdjustments = (payroll) => {
    setSelectedPayroll(payroll);
    setIsAdjustmentsOpen(true);
  };

  // Get adjustments for the selected payroll
  const getPayrollAdjustments = () => {
    return adjustments.filter(adj => adj.payrollId === selectedPayroll?.id);
  };

  // Add a new adjustment to a payroll
  const handleAddAdjustment = () => {
    if (!newAdjustment.amount || !newAdjustment.description) {
      toast({
        title: "Error",
        description: "Please fill all fields",
        variant: "destructive"
      });
      return;
    }

    const amount = parseFloat(newAdjustment.amount);
    const newAdjustmentItem = {
      id: adjustments.length + 1,
      payrollId: selectedPayroll.id,
      type: newAdjustment.type,
      amount: amount,
      description: newAdjustment.description
    };

    // Update adjustments
    setAdjustments([...adjustments, newAdjustmentItem]);

    // Update payroll total adjustments and net salary
    const updatedPayrolls = payrolls.map(p => {
      if (p.id === selectedPayroll.id) {
        const adjustmentAmount = newAdjustment.type === "BONUS" ? amount : -amount;
        const newTotalAdjustments = p.totalAdjustments + adjustmentAmount;
        return {
          ...p,
          totalAdjustments: newTotalAdjustments,
          netSalary: p.baseSalary + newTotalAdjustments
        };
      }
      return p;
    });

    setPayrolls(updatedPayrolls);
    setSelectedPayroll(updatedPayrolls.find(p => p.id === selectedPayroll.id));
    
    // Reset form
    setNewAdjustment({
      type: "BONUS",
      amount: "",
      description: ""
    });

    toast({
      title: "Success",
      description: "Adjustment added successfully",
    });
  };

  // Delete an adjustment
  const handleDeleteAdjustment = (adjustmentId) => {
    const adjustmentToDelete = adjustments.find(a => a.id === adjustmentId);
    
    // Update adjustments
    setAdjustments(adjustments.filter(a => a.id !== adjustmentId));

    // Update payroll total adjustments and net salary
    const updatedPayrolls = payrolls.map(p => {
      if (p.id === selectedPayroll.id) {
        const adjustmentAmount = adjustmentToDelete.type === "BONUS" 
          ? -adjustmentToDelete.amount 
          : adjustmentToDelete.amount;
        const newTotalAdjustments = p.totalAdjustments + adjustmentAmount;
        return {
          ...p,
          totalAdjustments: newTotalAdjustments,
          netSalary: p.baseSalary + newTotalAdjustments
        };
      }
      return p;
    });

    setPayrolls(updatedPayrolls);
    setSelectedPayroll(updatedPayrolls.find(p => p.id === selectedPayroll.id));

    toast({
      title: "Success",
      description: "Adjustment deleted successfully",
    });
  };

  return (
    <div className="page-container">
      <div className="flex items-center justify-between mb-6">
        <h1 className="text-2xl font-bold">Payroll Management</h1>
        <Button onClick={() => setIsCreatePayrollOpen(true)}>
          <Plus className="mr-2 h-4 w-4" />
          Create Payroll
        </Button>
      </div>

      <Card>
        <CardHeader>
          <CardTitle>Payrolls</CardTitle>
          <CardDescription>Manage employee payrolls and adjustments</CardDescription>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Employee</TableHead>
                <TableHead>Position</TableHead>
                <TableHead>Issue Date</TableHead>
                <TableHead>Base Salary</TableHead>
                <TableHead>Adjustments</TableHead>
                <TableHead>Net Salary</TableHead>
                <TableHead>Status</TableHead>
                <TableHead>Actions</TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
              {payrolls.map((payroll) => (
                <TableRow key={payroll.id}>
                  <TableCell className="font-medium">{payroll.employeeName}</TableCell>
                  <TableCell>{payroll.position}</TableCell>
                  <TableCell>{new Date(payroll.issueDate).toLocaleDateString()}</TableCell>
                  <TableCell>${payroll.baseSalary.toLocaleString('en-US', {minimumFractionDigits: 2})}</TableCell>
                  <TableCell className={payroll.totalAdjustments >= 0 ? "text-green-600" : "text-red-600"}>
                    {payroll.totalAdjustments >= 0 ? "+" : ""}{payroll.totalAdjustments.toLocaleString('en-US', {minimumFractionDigits: 2})}
                  </TableCell>
                  <TableCell className="font-bold">${payroll.netSalary.toLocaleString('en-US', {minimumFractionDigits: 2})}</TableCell>
                  <TableCell>
                    <span className={`px-2 py-1 rounded-md text-xs ${
                      payroll.status === "Paid" 
                        ? "bg-green-100 text-green-800" 
                        : "bg-yellow-100 text-yellow-800"
                    }`}>
                      {payroll.status}
                    </span>
                  </TableCell>
                  <TableCell>
                    <div className="flex space-x-2">
                      <Button variant="outline" size="sm" onClick={() => handleViewAdjustments(payroll)}>
                        <Eye size={14} className="mr-1" />
                        View Adjustments
                      </Button>
                    </div>
                  </TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </CardContent>
      </Card>

      {/* Create Payroll Dialog */}
      <Dialog open={isCreatePayrollOpen} onOpenChange={setIsCreatePayrollOpen}>
        <DialogContent>
          <DialogHeader>
            <DialogTitle>Create New Payroll</DialogTitle>
            <DialogDescription>
              Create a new payroll for an employee. The base salary will be automatically set based on the employee's position.
            </DialogDescription>
          </DialogHeader>
          
          <div className="grid gap-4 py-4">
            <div className="grid grid-cols-4 items-center gap-4">
              <Label htmlFor="employee" className="text-right">
                Employee
              </Label>
              <Select 
                onValueChange={handleEmployeeChange}
                value={newPayroll.employeeId}
              >
                <SelectTrigger className="col-span-3">
                  <SelectValue placeholder="Select employee" />
                </SelectTrigger>
                <SelectContent>
                  {EMPLOYEES.map(employee => (
                    <SelectItem key={employee.id} value={employee.id.toString()}>
                      {employee.name} - {employee.position}
                    </SelectItem>
                  ))}
                </SelectContent>
              </Select>
            </div>

            {selectedEmployee && (
              <div className="grid grid-cols-4 items-center gap-4">
                <Label className="text-right">Base Salary</Label>
                <div className="col-span-3 font-medium">
                  ${selectedEmployee.baseSalary.toLocaleString('en-US', {minimumFractionDigits: 2})}
                </div>
              </div>
            )}

            <div className="grid grid-cols-4 items-center gap-4">
              <Label htmlFor="issueDate" className="text-right">
                Issue Date
              </Label>
              <Input
                id="issueDate"
                type="date"
                value={newPayroll.issueDate}
                onChange={(e) => setNewPayroll({...newPayroll, issueDate: e.target.value})}
                className="col-span-3"
              />
            </div>

            <div className="grid grid-cols-4 items-center gap-4">
              <Label htmlFor="status" className="text-right">
                Status
              </Label>
              <Select 
                onValueChange={(value) => setNewPayroll({...newPayroll, status: value})}
                value={newPayroll.status}
              >
                <SelectTrigger className="col-span-3">
                  <SelectValue placeholder="Select status" />
                </SelectTrigger>
                <SelectContent>
                  <SelectItem value="Pending">Pending</SelectItem>
                  <SelectItem value="Paid">Paid</SelectItem>
                </SelectContent>
              </Select>
            </div>
          </div>

          <DialogFooter>
            <Button variant="outline" onClick={() => setIsCreatePayrollOpen(false)}>Cancel</Button>
            <Button onClick={handleCreatePayroll}>Create Payroll</Button>
          </DialogFooter>
        </DialogContent>
      </Dialog>

      {/* Adjustments Dialog */}
      <Dialog open={isAdjustmentsOpen} onOpenChange={setIsAdjustmentsOpen}>
        <DialogContent className="max-w-3xl">
          <DialogHeader>
            <DialogTitle>Payroll Adjustments</DialogTitle>
            <DialogDescription>
              {selectedPayroll && (
                <>
                  Manage adjustments for {selectedPayroll.employeeName}'s payroll - 
                  Issue Date: {new Date(selectedPayroll.issueDate).toLocaleDateString()}
                </>
              )}
            </DialogDescription>
          </DialogHeader>
          
          <div className="grid gap-6">
            {/* Current Adjustments */}
            <div>
              <h3 className="text-sm font-medium mb-2">Current Adjustments</h3>
              {selectedPayroll && getPayrollAdjustments().length > 0 ? (
                <Table>
                  <TableHeader>
                    <TableRow>
                      <TableHead>Type</TableHead>
                      <TableHead>Amount</TableHead>
                      <TableHead>Description</TableHead>
                      <TableHead>Actions</TableHead>
                    </TableRow>
                  </TableHeader>
                  <TableBody>
                    {getPayrollAdjustments().map((adjustment) => (
                      <TableRow key={adjustment.id}>
                        <TableCell>
                          <span className={`px-2 py-1 rounded-md text-xs ${
                            adjustment.type === "BONUS" 
                              ? "bg-green-100 text-green-800" 
                              : "bg-red-100 text-red-800"
                          }`}>
                            {adjustment.type}
                          </span>
                        </TableCell>
                        <TableCell>${adjustment.amount.toLocaleString('en-US', {minimumFractionDigits: 2})}</TableCell>
                        <TableCell>{adjustment.description}</TableCell>
                        <TableCell>
                          <Button 
                            variant="ghost" 
                            size="sm"
                            onClick={() => handleDeleteAdjustment(adjustment.id)}
                          >
                            <Trash size={14} className="text-red-500" />
                          </Button>
                        </TableCell>
                      </TableRow>
                    ))}
                  </TableBody>
                </Table>
              ) : (
                <div className="text-center py-4 text-muted-foreground">
                  No adjustments found for this payroll.
                </div>
              )}
            </div>

            {/* Add New Adjustment */}
            <div className="border-t pt-4">
              <h3 className="text-sm font-medium mb-4">Add New Adjustment</h3>
              <div className="grid grid-cols-4 gap-4">
                <div>
                  <Label htmlFor="type">Type</Label>
                  <Select 
                    onValueChange={(value) => setNewAdjustment({...newAdjustment, type: value})}
                    value={newAdjustment.type}
                  >
                    <SelectTrigger>
                      <SelectValue placeholder="Select type" />
                    </SelectTrigger>
                    <SelectContent>
                      <SelectItem value="BONUS">BONUS</SelectItem>
                      <SelectItem value="DISCOUNT">DISCOUNT</SelectItem>
                    </SelectContent>
                  </Select>
                </div>
                
                <div>
                  <Label htmlFor="amount">Amount</Label>
                  <Input
                    id="amount"
                    type="number"
                    step="0.01"
                    min="0"
                    placeholder="0.00"
                    value={newAdjustment.amount}
                    onChange={(e) => setNewAdjustment({...newAdjustment, amount: e.target.value})}
                  />
                </div>
                
                <div className="col-span-2">
                  <Label htmlFor="description">Description</Label>
                  <Input
                    id="description"
                    placeholder="Enter description"
                    value={newAdjustment.description}
                    onChange={(e) => setNewAdjustment({...newAdjustment, description: e.target.value})}
                  />
                </div>
              </div>
              
              <div className="flex justify-end mt-4">
                <Button onClick={handleAddAdjustment}>
                  <Plus className="mr-2 h-4 w-4" />
                  Add Adjustment
                </Button>
              </div>
            </div>

            {/* Summary */}
            {selectedPayroll && (
              <div className="bg-muted p-4 rounded-md mt-2">
                <div className="grid grid-cols-3 gap-4">
                  <div>
                    <p className="text-sm text-muted-foreground">Base Salary</p>
                    <p className="font-medium">${selectedPayroll.baseSalary.toLocaleString('en-US', {minimumFractionDigits: 2})}</p>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">Total Adjustments</p>
                    <p className={`font-medium ${selectedPayroll.totalAdjustments >= 0 ? "text-green-600" : "text-red-600"}`}>
                      {selectedPayroll.totalAdjustments >= 0 ? "+" : ""}{selectedPayroll.totalAdjustments.toLocaleString('en-US', {minimumFractionDigits: 2})}
                    </p>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">Net Salary</p>
                    <p className="font-bold">${selectedPayroll.netSalary.toLocaleString('en-US', {minimumFractionDigits: 2})}</p>
                  </div>
                </div>
              </div>
            )}
          </div>
        </DialogContent>
      </Dialog>
    </div>
  );
};

export default Payrolls;