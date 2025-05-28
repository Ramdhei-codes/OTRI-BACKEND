package com.ucaldas.otri.application.shared.services;

import com.ucaldas.otri.application.shared.Constants;
import com.ucaldas.otri.domain.technologies.entities.Question;
import com.ucaldas.otri.domain.technologies.entities.Technology;
import com.ucaldas.otri.domain.technologies.enums.InventiveLevel;
import com.ucaldas.otri.domain.technologies.enums.PreliminaryInterest;
import com.ucaldas.otri.domain.technologies.enums.ReadinessType;

import java.util.List;

public class BuildPromptService {

    public static String buildEvaluationPrompt(Technology technology, int level, ReadinessType type, List<Question> questions){
        StringBuilder sb = new StringBuilder();
        buildTechnologyInfo(technology, level, type, sb);
        buildQuestionsInfo(questions, sb);
        sb.append(Constants.evaluationPromptFormat);
        return sb.toString();
    }

    public static void buildQuestionsInfo(List<Question> questions, StringBuilder sb){
        for (Question question : questions){
            sb.append(question.getContent()).append("\n");
        }
    }

    private static void buildTechnologyInfo(Technology technology, int level, ReadinessType type, StringBuilder sb) {
        sb.append(Constants.promptHeader);
        sb.append("Sección - Identificación del resultado\n");
        sb.append("Nombre del resultado: ").append(technology.getResultName()).append("\n");
        sb.append("Grupo/Investigador responsable: ").append(technology.getResponsibleGroup()).append("\n");
        sb.append("Sección - Descripción técnica\n");
        sb.append("Resumen funcional del desarrollo: ").append(technology.getTechnicalDescription().getFunctionalSummary()).append("\n");
        sb.append("Resumen del problema que resuelve el desarrollo: ").append(technology.getTechnicalDescription().getProblemSolved()).append("\n");
        sb.append("Sector económico de aplicación de la tecnología: ").append(technology.getTechnicalDescription().getEconomicSector()).append("\n");
        sb.append("Sección - Protección intelectual\n");
        sb.append("¿Existe protección actual (patente, modelo de utilidad, etc.)?: ")
                .append(MapBoolean(technology.getIntellectualProtection().isHasCurrentProtection()))
                .append("\n");
        sb.append("Tipo de protección sugerida: ")
                .append(technology.getIntellectualProtection().getSuggestedProtectionType())
                .append("\n");
        sb.append("Sección - Análisis de patentabilidad\n");
        sb.append("Novedad (comparado con estado del arte): ")
                .append(technology.getPatentabilityAnalysis().getNoveltyDescription())
                .append("\n");
        sb.append("Nivel de actividad inventiva ")
                .append(MapInventiveLevel(technology.getPatentabilityAnalysis().getInventiveLevel()))
                .append("\n");
        sb.append("Aplicación industrial: ")
                .append(MapBoolean(technology.getPatentabilityAnalysis().isHasIndustrialApplication()))
                .append("\n");

        sb.append("Disponibilidad del Investigador y del equipo para adelantar porcesos de transferencia tecnológica: ")
                .append(MapBoolean(technology.getPatentabilityAnalysis().isTeamAvailableForTransfer()))
                .append("\n");
        sb.append("Ventaja diferencial frente a soluciones existentes: ")
                .append(technology.getPatentabilityAnalysis().getCompetitiveAdvantage())
                .append("\n");
        sb.append("Disponibilidad del equipo para colaborar: ")
                .append(MapBoolean(technology.getPatentabilityAnalysis().isTeamAvailableToCollaborate()))
                .append("\n");
        sb.append("Capacidad técnica de escalamiento: ")
                .append(MapBoolean(technology.getPatentabilityAnalysis().isHasScalingCapability()))
                .append("\n");
        sb.append("Identificación de competidores o tecnologías similares: ")
                .append(technology.getMarketAnalysis().getCompetitorsNational())
                .append("\n")
                .append(technology.getMarketAnalysis().getCompetitorsInternational())
                .append("\n");
        sb.append("Tamaño y tendencia del mercado objetivo: ")
                .append(technology.getMarketAnalysis().getMarketSize())
                .append("\n");
        sb.append("Sectores o industrias aplicables: ")
                .append(technology.getMarketAnalysis().getApplicationSectors())
                .append("\n");
        sb.append("Interés preliminar de actores externos: ")
                .append(MapPreliminaryInterest(technology.getMarketAnalysis().getPreliminaryInterest()))
                .append("\n");
        sb.append("Modalidad de transferencia sugerida (licencia, spin-off, etc.): ")
                .append(technology.getTransferMethod())
                .append("\n");
        sb.append(String.format("Responde las siguientes preguntas que corresponden a la medición del nivel de %s %d: ", type, level))
                .append(technology.getMarketAnalysis().getApplicationSectors())
                .append("\n");
    }

    private static String MapBoolean(boolean value){
        return value ? "Sí" : "No";
    }

    private static  String MapInventiveLevel(InventiveLevel level){
        return switch (level) {
            case LOW -> "Bajo";
            case MEDIUM -> "Medio";
            case HIGH -> "Alto";
            default -> null;
        };
    }

    private static  String MapPreliminaryInterest(PreliminaryInterest interest){
        return switch (interest) {
            case YES -> "Sí";
            case NO -> "No";
            case EXPLORING -> "En exploración";
            default -> null;
        };
    }
}
