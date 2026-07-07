import '../config/api_config.dart';
import '../models/models.dart';
import 'api_service.dart';

class PillowService {
  final _api = ApiService();

  Future<List<Pillow>> getAll({int page = 0, int size = 12}) async {
    final response = await _api.dio.get(
      ApiConfig.pillowsEndpoint,
      queryParameters: {'page': page, 'size': size, 'sort': 'id,desc'},
    );
    return (response.data['content'] as List)
        .map((json) => Pillow.fromJson(json))
        .toList();
  }

  Future<Pillow> getById(int id) async {
    final response = await _api.dio.get('${ApiConfig.pillowsEndpoint}/$id');
    return Pillow.fromJson(response.data);
  }

  Future<List<Pillow>> search(String? name, {int page = 0, int size = 12}) async {
    final params = <String, dynamic>{'page': page, 'size': size, 'sort': 'id,desc'};
    if (name != null && name.isNotEmpty) params['name'] = name;

    final response = await _api.dio.get(
      ApiConfig.pillowsEndpoint,
      queryParameters: params,
    );
    return (response.data['content'] as List)
        .map((json) => Pillow.fromJson(json))
        .toList();
  }

  Future<List<DefaultSize>> getSizes(int pillowId) async {
    final response = await _api.dio.get(
      ApiConfig.defaultSizesEndpoint,
      queryParameters: {'pillowId': pillowId},
    );
    return (response.data['content'] as List)
        .map((json) => DefaultSize.fromJson(json))
        .toList();
  }
}
