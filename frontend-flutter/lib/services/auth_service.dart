import '../config/api_config.dart';
import 'api_service.dart';

class AuthService {
  final _api = ApiService();

  Future<String> login(String username, String password) async {
    final response = await _api.dio.post(
      ApiConfig.loginEndpoint,
      data: {'username': username, 'password': password},
    );
    final token = response.data['id_token'];
    await _api.saveToken(token);
    return token;
  }

  Future<void> register(String login, String email, String password, String? firstName, String? lastName) async {
    await _api.dio.post(
      ApiConfig.registerEndpoint,
      data: {
        'login': login,
        'email': email,
        'password': password,
        'firstName': firstName,
        'lastName': lastName,
      },
    );
  }

  Future<Map<String, dynamic>> getAccount() async {
    final response = await _api.dio.get(ApiConfig.accountEndpoint);
    return response.data;
  }

  Future<void> logout() async {
    await _api.deleteToken();
  }

  Future<bool> isLoggedIn() async {
    final token = await _api.getToken();
    return token != null;
  }
}
