
import React from "react";
import { useAuth } from "@/context/AuthContext";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import { User, Briefcase, Clock, DollarSign, Mail, MapPin, Phone, Mail as MailIcon } from "lucide-react";

const Profile: React.FC = () => {
  const { user } = useAuth();
  
  // Mock employee data (would come from API in real app)
  const employeeData = {
    id: 2,
    name: "John Doe",
    position: "Senior Software Engineer",
    department: "Engineering",
    joinDate: "June 15, 2021",
    email: "john.doe@example.com",
    phone: "(555) 123-4567",
    address: "123 Main Street, Anytown, ST 12345",
    salary: "$85,000",
    manager: "Jane Smith",
    status: "Active"
  };

  // Mock quick stats (would come from API in real app)
  const quickStats = [
    { 
      title: "Available PTO", 
      value: "12 days", 
      icon: Clock, 
      color: "bg-blue-100 text-blue-600" 
    },
    { 
      title: "Next Payday", 
      value: "July 30", 
      icon: DollarSign, 
      color: "bg-green-100 text-green-600" 
    },
    { 
      title: "Pending Requests", 
      value: "1", 
      icon: Mail, 
      color: "bg-amber-100 text-amber-600" 
    },
  ];

  return (
    <div className="page-container">
      <div className="page-header">
        <h1>My Profile</h1>
      </div>

      <div className="grid gap-6 lg:grid-cols-3">
        <div className="lg:col-span-1">
          <Card>
            <CardHeader className="pb-2 text-center">
              <div className="flex justify-center mb-4">
                <div className="h-20 w-20 rounded-full bg-primary/10 flex items-center justify-center">
                  <User size={40} className="text-primary" />
                </div>
              </div>
              <CardTitle>{employeeData.name}</CardTitle>
              <div className="flex items-center justify-center gap-2 text-sm text-muted-foreground">
                <Briefcase size={14} />
                <span>{employeeData.position}</span>
              </div>
            </CardHeader>
            <CardContent>
              <div className="space-y-4 mt-4">
                <div className="flex items-center gap-3">
                  <div className="h-8 w-8 rounded-full bg-secondary flex items-center justify-center">
                    <MailIcon size={14} className="text-muted-foreground" />
                  </div>
                  <div>
                    <p className="text-xs text-muted-foreground">Email</p>
                    <p className="text-sm">{employeeData.email}</p>
                  </div>
                </div>
                
                <div className="flex items-center gap-3">
                  <div className="h-8 w-8 rounded-full bg-secondary flex items-center justify-center">
                    <Phone size={14} className="text-muted-foreground" />
                  </div>
                  <div>
                    <p className="text-xs text-muted-foreground">Phone</p>
                    <p className="text-sm">{employeeData.phone}</p>
                  </div>
                </div>
                
                <div className="flex items-center gap-3">
                  <div className="h-8 w-8 rounded-full bg-secondary flex items-center justify-center">
                    <MapPin size={14} className="text-muted-foreground" />
                  </div>
                  <div>
                    <p className="text-xs text-muted-foreground">Address</p>
                    <p className="text-sm">{employeeData.address}</p>
                  </div>
                </div>
              </div>
            </CardContent>
          </Card>
        </div>

        <div className="lg:col-span-2">
          <div className="grid gap-6 md:grid-cols-3">
            {quickStats.map((stat, index) => (
              <Card key={index}>
                <CardContent className="pt-6">
                  <div className="flex flex-col items-center">
                    <div className={`p-2 rounded-full ${stat.color} mb-2`}>
                      <stat.icon size={18} />
                    </div>
                    <p className="text-sm text-muted-foreground">{stat.title}</p>
                    <p className="text-xl font-bold">{stat.value}</p>
                  </div>
                </CardContent>
              </Card>
            ))}
          </div>

          <Card className="mt-6">
            <CardHeader>
              <CardTitle>Employment Details</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-4">
                <div className="grid grid-cols-2 gap-4">
                  <div>
                    <p className="text-sm text-muted-foreground">Department</p>
                    <p>{employeeData.department}</p>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">Position</p>
                    <p>{employeeData.position}</p>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">Manager</p>
                    <p>{employeeData.manager}</p>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">Status</p>
                    <p>{employeeData.status}</p>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">Join Date</p>
                    <p>{employeeData.joinDate}</p>
                  </div>
                  <div>
                    <p className="text-sm text-muted-foreground">Base Salary</p>
                    <p>{employeeData.salary}</p>
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

export default Profile;
