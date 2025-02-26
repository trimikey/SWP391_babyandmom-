package com.swp.BabyandMom.Entity.Enum;

public enum OrderStatus {
    PENDING,         // Chờ xác nhận (đơn mới tạo, chưa thanh toán)
    PAID,           // Đã thanh toán thành công, gói bắt đầu kích hoạt
    ACTIVE,         // Đang hoạt động (gói đang có hiệu lực)
    EXPIRED,        // Hết hạn (cần gia hạn để tiếp tục)
    CANCELED,       // Đã hủy bởi người dùng trước khi hết hạn
    FAILED,         // Thanh toán thất bại
}
