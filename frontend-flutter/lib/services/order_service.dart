import '../config/api_config.dart';
import '../models/models.dart';
import 'api_service.dart';

class OrderService {
  final _api = ApiService();

  Future<List<CustomerOrder>> getAll({int page = 0, int size = 10}) async {
    final response = await _api.dio.get(
      ApiConfig.ordersEndpoint,
      queryParameters: {'page': page, 'size': size, 'sort': 'id,desc'},
    );
    return (response.data['content'] as List)
        .map((json) => CustomerOrder.fromJson(json))
        .toList();
  }

  Future<CustomerOrder> getById(int id) async {
    final response = await _api.dio.get('${ApiConfig.ordersEndpoint}/$id');
    return CustomerOrder.fromJson(response.data);
  }

  Future<CustomerOrder> create(Map<String, dynamic> data) async {
    final response = await _api.dio.post(ApiConfig.ordersEndpoint, data: data);
    return CustomerOrder.fromJson(response.data);
  }

  Future<OrderItem> createItem(Map<String, dynamic> data) async {
    final response = await _api.dio.post(ApiConfig.orderItemsEndpoint, data: data);
    return OrderItem.fromJson(response.data);
  }
}
