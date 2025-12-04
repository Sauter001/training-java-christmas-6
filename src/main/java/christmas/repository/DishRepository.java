package christmas.repository;

import christmas.domain.Dish;
import christmas.domain.DishType;

import java.util.ArrayList;
import java.util.List;

public class DishRepository {
    private final List<Dish> dishes = new ArrayList<>();

    public DishRepository() {
        insertAppetizer();
        insertMainDish();
        insertDessert();
        insertDrink();
    }

    public List<Dish> findDishesOf(DishType type) {
        return dishes.stream().filter(d -> d.typeEquals(type)).toList();
    }

    private void insertAppetizer() {
        DishType appetizerType = DishType.APPETIZER;
        dishes.add(new Dish("양송이수프", 6000, appetizerType));
        dishes.add(new Dish("타파스", 5500, appetizerType));
        dishes.add(new Dish("시저샐러드", 8000, appetizerType));
    }

    private void insertMainDish() {
        DishType mainDishType = DishType.MAIN;
        dishes.add(new Dish("티본스테이크", 55000, mainDishType));
        dishes.add(new Dish("바베큐립", 54000, mainDishType));
        dishes.add(new Dish("해산물파스타", 35000, mainDishType));
        dishes.add(new Dish("크리스마스파스타", 25000, mainDishType));
    }

    private void insertDessert() {
        DishType dessertType = DishType.DESSERT;
        dishes.add(new Dish("초코케이크", 15000, dessertType));
        dishes.add(new Dish("아이스크림", 5000, dessertType));
    }

    private void insertDrink() {
        DishType drinkType = DishType.DRINK;
        dishes.add(new Dish("제로콜라", 3000, drinkType));
        dishes.add(new Dish("레드와인", 60000, drinkType));
        dishes.add(new Dish("샴페인", 25000, drinkType));
    }
}
