package com.example.webservice.utill.mapper;

import com.example.webservice.model.dto.EmployeeFormDto;
import com.example.webservice.model.entity.Employee;
import org.mapstruct.Mapper;

@Mapper
public interface EmployeeMapper {
    Employee employeeDtoToEmployee(EmployeeFormDto employeeFormDto);

    EmployeeFormDto employeeToEmployeeDto(Employee employee);
}
