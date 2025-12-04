package christmas.domain.dto;

public record OrderCountDto(DishNameDto dishNameDto, int count) {
    public String getDishName() {
        return dishNameDto.dishName();
    }
}
