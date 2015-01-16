package youapp.frontend.controllers;

import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

@Controller
@RequestMapping("/*")
public class JavascriptController
    extends ExceptionHandlingController
{
    @RequestMapping(value = "javascript/globalVariables")
    public ModelAndView strings(HttpServletRequest request)
    {
        // Retrieve the locale of the User
        Locale locale = RequestContextUtils.getLocale(request);
        // Use the path to your bundle
        ResourceBundle bundle = ResourceBundle.getBundle("messages", locale);
        // Call the string.jsp view
        return new ModelAndView("globalVariablesJavascript", "keys", bundle.getKeys());
    }

}
