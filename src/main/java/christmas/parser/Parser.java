package christmas.parser;

public interface Parser<T> {
    T parse(String input);
}
