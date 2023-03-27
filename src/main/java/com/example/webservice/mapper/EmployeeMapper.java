package com.example.webservice.mapper;

import com.example.webservice.dto.EmployeeFormDto;
import com.example.webservice.entity.Employee;
import org.mapstruct.Mapper;

@Mapper
public interface EmployeeMapper {
    Employee employeeDtoToEmployee(EmployeeFormDto employeeFormDto);

    EmployeeFormDto employeeToEmployeeDto(Employee employee);
}
