package com.codeit.sb01hrbankteam04.global.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/")
@RequiredArgsConstructor
public class IndexViewController {

  @GetMapping
  public String index() {
    return "forward:/index.html";  // template/index.html 렌더링
  }
}
