package com.example.demo.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.model.Demo;
import com.example.demo.repo.DemoRepository;

@Controller
@RequestMapping("/demo")
public class DemoController {
	@Autowired
	private DemoRepository DemoRepo;
	
	private final  String UPLOAD_DIR = System.getProperty("user.dir") + "/src/main/resources/static/";
	
	@GetMapping("/")
	public String inicio(Model model) {
		model.addAttribute("demo", DemoRepo.findAll());
		return "index";
	}
	@GetMapping("/form")
	public String form(Model model) {
		model.addAttribute("paciente", new Demo());
		return "form";
	}
	@GetMapping("/form/{id}")
	public String form(@PathVariable("id") Long id, Model model) {
		Optional<Demo> paciente = DemoRepo.findById(id);
		if (paciente.isPresent()) {
			model.addAttribute("paciente", paciente.get());
		} else {
			model.addAttribute("paciente", new Demo());
		}
		return "form";
	}
	
	@PostMapping("/add")
	public String addPaciente(@RequestParam("id") Optional<Long> id, @RequestParam("nome") String nome, @RequestParam("descricao") String descricao, @RequestParam("data") String data, @RequestParam("imagem") MultipartFile imagem)  {
		
	Demo paciente;
		if (id.isPresent()) {
			paciente = DemoRepo.findById(id.get()).orElse(new Demo());
		} else {
			paciente = new Demo();
		} 
		paciente.setNome(nome);
		paciente.setData(Date.valueOf(data));
		paciente.setDescricao(descricao);
		
		DemoRepo.save(paciente); //Salvar dentro do banco de dados
		
		// ! = diferente
		if (!imagem.isEmpty()) {
			try {
				// Lógica para salvar a imagem
				String fileName = "paciente_" + paciente.getId() + "_" + imagem.getOriginalFilename();
				// Java.nio.file
				Path path = Paths.get(UPLOAD_DIR + fileName);
				Files.write(path, imagem.getBytes());
				paciente.setImagem("/" + fileName);
				
				DemoRepo.save(paciente);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return "redirect:/demo/";
		
	}
	
	@GetMapping("/delete/{id}")
	public String deleteFilme(@PathVariable("id") Long id) {
		Optional<Demo> paciente = DemoRepo.findById(id);
		
		if(paciente.isPresent()) {
			Demo pacienteParaDeletar = paciente.get();
			String imagePath = UPLOAD_DIR + pacienteParaDeletar.getImagem();
			try {
				Files.deleteIfExists(Paths.get(imagePath));
			} catch (Exception e) {
				e.printStackTrace();
			}
			DemoRepo.deleteById(id);
		}
		return "redirect:/demo/";
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
