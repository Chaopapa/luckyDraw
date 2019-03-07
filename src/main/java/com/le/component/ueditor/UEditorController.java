package com.le.component.ueditor;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


@Controller
@RequestMapping("/admin/component/ueditor")
public class UEditorController {

    @RequestMapping(value = "/config")
    @ResponseBody
    public void config(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String rootPath = request.getSession().getServletContext().getRealPath("/");
        String exec = new ActionEnter(request, rootPath).exec();
        response.getWriter().write(exec);
//        return exec;
    }
}
