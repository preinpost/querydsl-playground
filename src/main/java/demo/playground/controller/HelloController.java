package demo.playground.controller;

import demo.playground.service.TestService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HelloController {

    private final TestService testService;

    @GetMapping("/hello")
    public String hello() {
        log.info("hello");

        testService.doService();

        return "Hello World";
    }
}
