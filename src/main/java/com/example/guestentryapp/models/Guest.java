package com.example.guestentryapp.models;

import java.time.LocalDate;

public class Guest {
    private int id;
    private LocalDate date;
    private String entryTime;
    private String exitTime;
    private String name;
    private String purpose;
    private boolean medicalStatement;
    private boolean instructionStatement;
    private byte[] signature;

    public Guest(int id, LocalDate date, String entryTime, String exitTime, String name, String purpose,
                 boolean medicalStatement, boolean instructionStatement, byte[] signature) {
        this.id = id;
        this.date = date;
        this.entryTime = entryTime;
        this.exitTime = exitTime;
        this.name = name;
        this.purpose = purpose;
        this.medicalStatement = medicalStatement;
        this.instructionStatement = instructionStatement;
        this.signature = signature;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getEntryTime() {
        return entryTime;
    }

    public void setEntryTime(String entryTime) {
        this.entryTime = entryTime;
    }

    public String getExitTime() {
        return exitTime;
    }

    public void setExitTime(String exitTime) {
        this.exitTime = exitTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPurpose() {
        return purpose;
    }

    public void setPurpose(String purpose) {
        this.purpose = purpose;
    }

    public boolean isMedicalStatement() {
        return medicalStatement;
    }

    public void setMedicalStatement(boolean medicalStatement) {
        this.medicalStatement = medicalStatement;
    }

    public boolean isInstructionStatement() {
        return instructionStatement;
    }

    public void setInstructionStatement(boolean instructionStatement) {
        this.instructionStatement = instructionStatement;
    }

    public byte[] getSignature() {
        return signature;
    }

    public void setSignature(byte[] signature) {
        this.signature = signature;
    }
}
