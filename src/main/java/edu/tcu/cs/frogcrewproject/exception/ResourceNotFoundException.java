package edu.tcu.cs.frogcrewproject.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, Object identifier) {
        super(resource + " not found: " + identifier);
    }
}
