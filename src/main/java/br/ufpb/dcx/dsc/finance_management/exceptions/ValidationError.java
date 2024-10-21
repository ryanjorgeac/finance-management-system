package br.ufpb.dcx.dsc.finance_management.exceptions;

public record ValidationError(String field, String message) {}
