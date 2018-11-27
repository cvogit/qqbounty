package com.revature.controllers;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.revature.models.User;
import com.revature.services.UserService;
import com.revature.util.JwtUtil;
import com.revature.util.ResponseMap;

@RestController
@RequestMapping(path = "users")
public class UserController {

	@Autowired
	private UserService sUserService;
	
	@Autowired
	private ResponseMap sResponseMap;

	@PostMapping
	public ResponseEntity<Map<String, Object>> save(@RequestBody User pUser) {
		Map<String, Object> tServiceResult = sUserService.save(pUser);
		if(tServiceResult == null) {
			return ResponseEntity.badRequest().body(sResponseMap.getBadResponse());
		}
		return ResponseEntity.ok().body(sResponseMap.getGoodResponse(tServiceResult));
	}
	
	@PostMapping("/login")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> login(@RequestBody User pUser, HttpServletRequest req) {
		Object tServiceResult = sUserService.login(pUser);
		if(tServiceResult == null) {
			return ResponseEntity.badRequest().body(sResponseMap.getBadResponse());
		}
		return ResponseEntity.ok().body(sResponseMap.getGoodResponse(tServiceResult));
	}
	
	@GetMapping
	@ResponseBody
	public ResponseEntity<Map<String, Object>> findAll(HttpServletRequest req) throws IOException {
		if(!JwtUtil.isRequestFromAdmin(req)) {
			return ResponseEntity.badRequest().body(sResponseMap.getBadResponse());
		}
		Map<String, Object> tServiceResult = (Map<String, Object>) sUserService.findAll();
		if(tServiceResult == null) {
			return ResponseEntity.badRequest().body(sResponseMap.getBadResponse());
		}
		return ResponseEntity.ok().body(sResponseMap.getGoodResponse(tServiceResult));
	}

	@GetMapping("{id}")
	public ResponseEntity<Map<String, Object>> findById(@PathVariable int id, HttpServletRequest req) throws IOException {
		if(!JwtUtil.isRequestFromSelf(req, id)) {
			return ResponseEntity.badRequest().body(sResponseMap.getBadResponse());
		}
		Map<String, Object> tServiceResult = (Map<String, Object>) sUserService.findById(id);
		if(tServiceResult == null) {
			return ResponseEntity.badRequest().body(sResponseMap.getBadResponse());
		}
		return ResponseEntity.ok().body(sResponseMap.getGoodResponse(tServiceResult));
	}

	@PatchMapping("{id}")
	public ResponseEntity<Map<String, Object>> update(@PathVariable int id, @Valid @RequestBody User pUser, HttpServletRequest req) throws IOException {
		if(!JwtUtil.isRequestFromSelf(req, id)) {
			return ResponseEntity.badRequest().body(sResponseMap.getBadResponse());
		}
		pUser.setUserId(id);
		Map<String, Object> tServiceResult = (Map<String, Object>) sUserService.update(pUser);
		if(tServiceResult == null) {
			return ResponseEntity.badRequest().body(sResponseMap.getBadResponse());
		}
		return ResponseEntity.ok().body(sResponseMap.getGoodResponse(tServiceResult));
	}
}
