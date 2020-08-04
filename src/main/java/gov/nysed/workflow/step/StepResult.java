package gov.nysed.workflow.step;

public interface StepResult<T> {

    T getOutput();

    String getEventName();

    boolean isComplete();
}
