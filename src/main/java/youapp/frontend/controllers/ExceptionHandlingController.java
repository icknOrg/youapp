package youapp.frontend.controllers;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import youapp.exception.GenericException;
import youapp.exception.NotFoundException;
import youapp.exception.dataaccess.DataAccessLayerException;
import youapp.exception.model.ModelException;
import youapp.exception.output.JsonError;

@Controller
public class ExceptionHandlingController
{
    /**
     * Logger.
     */
    private final Log log = LogFactory.getLog(this.getClass());

    private MessageSource messageSource;

    @Autowired
    public void setMessageSource(MessageSource messageSource)
    {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleAllExceptions(Exception ex, Locale locale, HttpServletRequest request)
    {
        if (log.isErrorEnabled())
        {
            log.error(ex.getMessage());
        }

        String message =
            messageSource.getMessage("error.messages.errorUnexpected", null, null) + ex.getLocalizedMessage();
        if (isAjax(request))
        {
            return new ModelAndView("jsonView", "result", new JsonError(message, messageSource.getMessage(
                "error.headings.title", null, locale)));
        }
        else
        {
            return new ModelAndView("error/error", "message", message);
        }
    }

    @ExceptionHandler(DataAccessLayerException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleDataAccessLayerExceptions(Exception ex, Locale locale, HttpServletRequest request)
    {
        if (log.isErrorEnabled())
        {
            log.error(ex.getMessage());
        }

        String message =
            messageSource.getMessage("error.messages.errorAccessingDb", null, null) + ex.getLocalizedMessage();
        if (isAjax(request))
        {
            return new ModelAndView("jsonView", "result", new JsonError(message, messageSource.getMessage(
                "error.headings.title", null, locale)));
        }
        else
        {
            return new ModelAndView("error/error", "message", message);
        }
    }

    @ExceptionHandler(ModelException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleModelExceptions(Exception ex, Locale locale, HttpServletRequest request)
    {
        if (log.isErrorEnabled())
        {
            log.error(ex.getMessage());
        }

        String message =
            messageSource.getMessage("error.messages.errorAccessingDb", null, null) + ex.getLocalizedMessage();

        if (isAjax(request))
        {
            return new ModelAndView("jsonView", "result", new JsonError(message, messageSource.getMessage(
                "error.headings.title", null, locale)));
        }
        else
        {
            return new ModelAndView("error/error", "message", message);
        }
    }

    @ExceptionHandler(GenericException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ModelAndView handleGenericExceptions(Exception ex, Locale locale, HttpServletRequest request)
    {
        if (log.isErrorEnabled())
        {
            log.error(ex.getMessage());
        }

        String message = ex.getMessage();
        if (isAjax(request))
        {
            return new ModelAndView("jsonView", "result", new JsonError(message, messageSource.getMessage(
                "error.headings.title", null, locale)));
        }
        else
        {
            return new ModelAndView("error/error", "message", message);
        }
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ModelAndView handleNotFoundExceptions(Exception ex, Locale locale, HttpServletRequest request)
    {
        if (log.isErrorEnabled())
        {
            log.error(ex.getMessage());
        }

        String message = ex.getMessage();
        if (isAjax(request))
        {
            return new ModelAndView("jsonView", "result", new JsonError(message, messageSource.getMessage(
                "error.headings.title", null, locale)));
        }
        else
        {
            return new ModelAndView("error/error", "message", message);
        }
    }

    public static boolean isAjax(HttpServletRequest request)
    {
        return "XMLHttpRequest".equals(request.getHeader("X-Requested-With"));
    }
}
