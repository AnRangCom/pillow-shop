import '../config/api_config.dart';
import '../models/models.dart';
import 'api_service.dart';

class ChatService {
  final _api = ApiService();

  Future<ChatResponse> sendMessage(String message, String? sessionId) async {
    final response = await _api.dio.post(
      ApiConfig.chatEndpoint,
      data: ChatMessage(message: message, sessionId: sessionId).toJson(),
    );
    return ChatResponse.fromJson(response.data);
  }

  Future<ChatResponse> getInitialSuggestions() async {
    final response = await _api.dio.get('${ApiConfig.chatEndpoint}/suggestions');
    return ChatResponse.fromJson(response.data);
  }

  Future<ChatResponse> compareProducts(List<int> pillowIds, String? sessionId) async {
    final response = await _api.dio.post(
      '${ApiConfig.chatEndpoint}/compare',
      data: pillowIds,
      queryParameters: {'sessionId': sessionId},
    );
    return ChatResponse.fromJson(response.data);
  }
}
