package org.krish.learn.service;

import java.util.List;
import java.util.Optional;

import org.krish.learn.entity.Student;
import org.krish.learn.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

	@Autowired
	private StudentRepository studentRepository;

	public List<Student> getAllStudents() {
		return studentRepository.findAll();
	}

	@Cacheable(value = "student", key="#id")
	public Optional<Student> getStudentById(int id) {
		return studentRepository.findById(id);
	}

	@CachePut(value="student", key="#student.id")
	public Student addStudent(Student student) {
		return studentRepository.save(student);
	}

	@CacheEvict(value = "student", key = "#id")
	public void deleteStudent(int id) {
		Optional<Student> student = studentRepository.findById(id);
		if(student.isPresent()) {
			System.out.println("Student ID::"+student.get().getId());
			studentRepository.delete(student.get());
		}else {
			System.out.println(id+" is not present");
		}
	}

	@CacheEvict(value = "student", key = "#student.id")
	public Student updateStudent(Student student) {
		if (studentRepository.findById(student.getId()).isPresent()) {
			Student objStudent = studentRepository.findById(student.getId()).get();
			objStudent.setName(student.getName());
			objStudent.setEmail(student.getEmail());
			return studentRepository.save(objStudent);

		} else {
			return null;
		}
	}

}
