package activiti.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.repository.ProcessDefinition;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class DeployProcessController extends AbstractController {

	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String getModelView(HttpServletRequest request, HttpServletResponse response, Model model) {

		List<ProcessDefinition> list = repositoryService.createProcessDefinitionQuery().list();
		model.addAttribute("processDefinitionList", list);
		return "activiti/process-list";
	}

	@RequestMapping(value = "/deployAll", method = RequestMethod.POST)
	public String deployAll(HttpServletRequest request, HttpServletResponse response, Model model,
			@RequestParam(value = "file", required = true) MultipartFile file) {
		String fileName = file.getOriginalFilename();
		System.out.println("文件名：" + fileName);
		InputStream zipStream;
		try {
			zipStream = file.getInputStream();
			repositoryService.createDeployment().addZipInputStream(new ZipInputStream(zipStream)).deploy();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "redirect:/login";
	}

}
