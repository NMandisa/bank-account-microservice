package za.co.mkhungo.bank.response;

/**
 * @author Noxolo.Mkhungo
 */
public record ApiResponse<T>(  String status,
                               T data,
                               String error
) {
    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>("success", data, null);
    }

    public static <T> ApiResponse<T> error(T data) {
        return new ApiResponse<>("error", data,"...");
    }
}

