package com.ucaldas.otri.application.shared;

public final class Constants {
    public static final String promptHeader = """
            Actúa como un expero evaluador de madurez tecnológica y comercial y, dada la siguiente información de una tecnología:
            
            """;
    public static final String evaluationPromptFormat = """
            La respuesta debe ser ÚNICAMENTE un texto donde cada línea contenga la pregunta y su respuesta (de sí o no de acuerdo a si la tecnología satisface la pregunta) separados por un punto y coma, ejemplo: El cliente está identificado;Sí
            """;
}
