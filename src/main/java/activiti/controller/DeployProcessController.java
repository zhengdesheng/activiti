package activiti.controller;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipInputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import activiti.entity.Leave;
import activiti.service.LeaveService;

@Controller
public class DeployProcessController extends AbstractController {

	@Autowired
	private LeaveService thisService;

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

		try {
			InputStream zipStream = file.getInputStream();
			String extension = FilenameUtils.getExtension(fileName);
			if (extension.equals("zip") || extension.equals("bar")) {

				repositoryService.createDeployment().addZipInputStream(new ZipInputStream(zipStream)).deploy();
			} else {
				repositoryService.createDeployment().addInputStream(fileName, zipStream).deploy();
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "redirect:/login";
	}

	@RequestMapping(value = "/nameLink")
	public void nameLink(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "pdid", required = true) String pdId,
			@RequestParam(value = "resourceName", required = true) String resourceName) throws IOException {
		ProcessDefinitionQuery query = repositoryService.createProcessDefinitionQuery();
		ProcessDefinition process = query.processDefinitionId(pdId).singleResult();
		InputStream inputStream = repositoryService.getResourceAsStream(process.getDeploymentId(), resourceName);
		byte[] b = new byte[1024];

		int len = -1;

		while ((len = inputStream.read(b, 0, 1024)) != -1) {
			response.getOutputStream().write(b, 0, len);
		}

	}

	@RequestMapping(value = "/delete-deployment")
	public String delete_deployment(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "deploymentId", required = true) String pdId) {

		repositoryService.deleteDeployment(pdId, true);
		return "redirect:/login";
	}

}
