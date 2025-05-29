package com.ucaldas.otri.application.shared;

public final class Constants {
    public static final String promptHeader = """
            Actúa como un expero evaluador de madurez tecnológica y comercial y, dada la siguiente información de una tecnología:
            
            """;
    public static final String evaluationPromptFormat = """
            La respuesta debe ser ÚNICAMENTE un texto donde cada línea contenga la pregunta y su respuesta (de sí o no de acuerdo a si la tecnología satisface la pregunta) separados por un punto y coma, ejemplo: El cliente está identificado;Sí
            """;
    public static final String recommendationsPromptFormat = """
            Dame una lista de recomendaciones en teniendo en cuenta que esta tecnología se encuentra actualmente en TRL %d y CRL %d en el siguiente formato en texto enriquecido con el propósito de enviarlo como respuesta a mi frontend:
            Título de la tecnología/proyecto: [Nombre del proyecto o tecnología]
            
            Nivel de TRL: [Nivel actual de TRL]
            
            Nivel de CRL: [Nivel actual de CRL]
            
            Recomendaciones para aumentar el TRL:
            
            [Recomendación específica basada en el estado actual y los objetivos del proyecto].
            
            [Otra recomendación relevante].
            
            [Consejo adicional si aplica].
            
            Recomendaciones para aumentar el CRL:
            
            [Recomendación específica para mejorar la madurez comercial].
            
            [Otra recomendación enfocada en la estrategia de comercialización].
            
            [Otro consejo útil].
            
            Notas adicionales:
            
            [Cualquier otro comentario o recomendación relevante].
            
            [Sugerencias adicionales según el contexto o la información proporcionada].
            """;
}
