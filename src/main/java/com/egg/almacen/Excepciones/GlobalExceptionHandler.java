//package com.egg.almacen.Excepciones;
//
//import java.sql.BatchUpdateException;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ControllerAdvice;
//import org.springframework.web.bind.annotation.ExceptionHandler;
//import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//
//@ControllerAdvice
//public class GlobalExceptionHandler {
//
//    // Manejar excepciones personalizadas
//    @ExceptionHandler(MiException.class)
//    public ResponseEntity<String> handleMiException(MiException ex) {
//        // Aquí puedes personalizar la respuesta
//        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
//    }
//
//    // Manejar otras excepciones generales
//    @ExceptionHandler(Exception.class)
//    public ResponseEntity<String> handleException(Exception ex) {
//        // Aquí puedes personalizar la respuesta para excepciones generales
//        return new ResponseEntity<>("Error interno del servidor", HttpStatus.INTERNAL_SERVER_ERROR);
//    }
//    
//    @ExceptionHandler(BatchUpdateException.class)
//    public String handleBatchUpdateExceptionRedirect(BatchUpdateException ex, RedirectAttributes redirectAttributes) {
//        redirectAttributes.addFlashAttribute("error", "Ocurrió un error al actualizar la base de datos: " + ex.getMessage());
//        return "redirect:/nombreDeLaVista";  // Redirigir a la vista
//    }
//    
//}
