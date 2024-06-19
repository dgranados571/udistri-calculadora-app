package com.co.calculadora.controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import java.io.InputStreamReader;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.co.calculadora.EnumConstantes;
import com.co.calculadora.dto.FormObject;

@Controller
public class AppController {
	
	String hostApi = "calculadora_api"; //127.0.0.1

	@GetMapping("/web/udistri")
	public String getUdistriControl(HttpServletRequest request, HttpServletResponse reponse, Model model) {
		
		HttpSession session = request.getSession();
		
		if (session.getAttribute(EnumConstantes.MESSAGE_INFO) != null) {
			String messageInfo = (String) session.getAttribute(EnumConstantes.MESSAGE_INFO);
			model.addAttribute(EnumConstantes.MESSAGE_INFO, messageInfo);
			session.removeAttribute(EnumConstantes.MESSAGE_INFO);
		}

		if (session.getAttribute(EnumConstantes.MESSAGE_ERROR) != null) {
			String messageError = (String) session.getAttribute(EnumConstantes.MESSAGE_ERROR);
			model.addAttribute(EnumConstantes.MESSAGE_ERROR, messageError);
			session.removeAttribute(EnumConstantes.MESSAGE_ERROR);
		}

		FormObject formObject = new FormObject();
		model.addAttribute("formObject", formObject);
		return "index";
	}

	@PostMapping("/web/operation")
	public String getOperatinAction(HttpServletRequest request, HttpServletResponse reponse, Model model,
			@ModelAttribute FormObject formObject) {
		HttpSession session = request.getSession();
		String response = "";
		switch (formObject.getOperatiosnAction()) {
		case "SUMA":
			response = sendGET("http://" + hostApi + ":5000/suma/" + formObject.getNum1Action() + "/" + formObject.getNum2Action());
			break;
		case "RESTA":
			response = sendGET("http://" + hostApi + ":5000/resta/" + formObject.getNum1Action() + "/" + formObject.getNum2Action());
			break;
		case "MULTIPLICA":
			response = sendGET("http://" + hostApi + ":5000/multiplicacion/" + formObject.getNum1Action() + "/" + formObject.getNum2Action());
			break;
		case "DIVIDE":
			response = sendGET("http://" + hostApi + ":5000/division/" + formObject.getNum1Action() + "/" + formObject.getNum2Action());
			break;
		default:
			break;
		}
		session.setAttribute(EnumConstantes.MESSAGE_INFO, "Resultado es: " + response);
		return "redirect:/web/udistri";
	}

	public String sendGET(String urlStr) {
		String responseObject = "";
		try {
			URL obj = new URL(urlStr);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			con.setRequestMethod("GET");
			int responseCode = con.getResponseCode();
			if (responseCode == HttpURLConnection.HTTP_OK) {
				BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
				String inputLine;
				StringBuffer response = new StringBuffer();
				while ((inputLine = in.readLine()) != null) {
					response.append(inputLine);
				}
				in.close();
				responseObject = response.toString();
			} else {
				responseObject = "GET request no logra resolver.";
			}
		} catch (MalformedURLException | ProtocolException e) {
			responseObject = "GET-1 Exepcion request no logra resolver. " + e;
		} catch (IOException e) {
			responseObject = "GET-2 Exepcion request no logra resolver. " + e;
		}
		return responseObject;
	}

}
