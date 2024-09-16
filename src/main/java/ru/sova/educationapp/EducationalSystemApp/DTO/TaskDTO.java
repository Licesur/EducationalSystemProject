package ru.sova.educationapp.EducationalSystemApp.DTO;

public class TaskDTO {

    private int id;

    private String definition;

    private String answer;

    @Override
    public String toString() {
        return "Task{" +id +
                ", definition='" + definition + '\'' +
                "answer='" + answer + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        TaskDTO taskDTO = (TaskDTO) o;
        return id == taskDTO.id && definition.equals(taskDTO.definition) && answer.equals(taskDTO.answer);
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + definition.hashCode();
        result = 31 * result + answer.hashCode();
        return result;
    }

    public TaskDTO() {
    }

    public TaskDTO(int id, String definition, String answer) {
        this.id = id;
        this.definition = definition;
        this.answer = answer;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDefinition() {
        return definition;
    }

    public void setDefinition(String definition) {
        this.definition = definition;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
