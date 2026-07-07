class Pillow {
  final int id;
  final String name;
  final String? description;
  final String? material;
  final double basePrice;

  Pillow({
    required this.id,
    required this.name,
    this.description,
    this.material,
    required this.basePrice,
  });

  factory Pillow.fromJson(Map<String, dynamic> json) {
    return Pillow(
      id: json['id'],
      name: json['name'],
      description: json['description'],
      material: json['material'],
      basePrice: (json['basePrice'] as num).toDouble(),
    );
  }
}

class DefaultSize {
  final int id;
  final String name;
  final double length;
  final double width;
  final double extraPrice;
  final int pillowId;

  DefaultSize({
    required this.id,
    required this.name,
    required this.length,
    required this.width,
    required this.extraPrice,
    required this.pillowId,
  });

  factory DefaultSize.fromJson(Map<String, dynamic> json) {
    return DefaultSize(
      id: json['id'],
      name: json['name'],
      length: (json['length'] as num).toDouble(),
      width: (json['width'] as num).toDouble(),
      extraPrice: (json['extraPrice'] as num).toDouble(),
      pillowId: json['pillowId'],
    );
  }
}

class CustomerOrder {
  final int id;
  final String orderCode;
  final String orderDate;
  final String status;
  final double totalAmount;
  final int customerId;

  CustomerOrder({
    required this.id,
    required this.orderCode,
    required this.orderDate,
    required this.status,
    required this.totalAmount,
    required this.customerId,
  });

  factory CustomerOrder.fromJson(Map<String, dynamic> json) {
    return CustomerOrder(
      id: json['id'],
      orderCode: json['orderCode'],
      orderDate: json['orderDate'],
      status: json['status'],
      totalAmount: (json['totalAmount'] as num).toDouble(),
      customerId: json['customerId'],
    );
  }

  String get statusLabel {
    switch (status) {
      case 'PENDING':
        return 'Chờ xác nhận';
      case 'CONFIRMED':
        return 'Đã xác nhận';
      case 'SHIPPED':
        return 'Đang giao';
      case 'DELIVERED':
        return 'Đã giao';
      case 'CANCELLED':
        return 'Đã hủy';
      default:
        return status;
    }
  }
}

class OrderItem {
  final int id;
  final int quantity;
  final String sizeType;
  final double? customLength;
  final double? customWidth;
  final double price;
  final int pillowId;
  final String? pillowName;
  final int? defaultSizeId;
  final int orderId;

  OrderItem({
    required this.id,
    required this.quantity,
    required this.sizeType,
    this.customLength,
    this.customWidth,
    required this.price,
    required this.pillowId,
    this.pillowName,
    this.defaultSizeId,
    required this.orderId,
  });

  factory OrderItem.fromJson(Map<String, dynamic> json) {
    return OrderItem(
      id: json['id'],
      quantity: json['quantity'],
      sizeType: json['sizeType'],
      customLength: json['customLength']?.toDouble(),
      customWidth: json['customWidth']?.toDouble(),
      price: (json['price'] as num).toDouble(),
      pillowId: json['pillowId'],
      pillowName: json['pillowName'],
      defaultSizeId: json['defaultSizeId'],
      orderId: json['orderId'],
    );
  }
}

class ChatMessage {
  final String message;
  final String? sessionId;

  ChatMessage({required this.message, this.sessionId});

  Map<String, dynamic> toJson() => {
    'message': message,
    'sessionId': sessionId,
  };
}

class ChatResponse {
  final String reply;
  final List<Pillow> suggestions;
  final String sessionId;

  ChatResponse({
    required this.reply,
    required this.suggestions,
    required this.sessionId,
  });

  factory ChatResponse.fromJson(Map<String, dynamic> json) {
    return ChatResponse(
      reply: json['reply'],
      suggestions: (json['suggestions'] as List)
          .map((p) => Pillow.fromJson(p))
          .toList(),
      sessionId: json['sessionId'],
    );
  }
}
