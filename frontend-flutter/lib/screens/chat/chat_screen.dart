import 'package:flutter/material.dart';
import 'package:flutter_chat_types/flutter_chat_types.dart' as types;
import 'package:flutter_chat_ui/flutter_chat_ui.dart';
import '../../services/chat_service.dart';
import '../../models/models.dart';

class ChatScreen extends StatefulWidget {
  const ChatScreen({super.key});

  @override
  State<ChatScreen> createState() => _ChatScreenState();
}

class _ChatScreenState extends State<ChatScreen> {
  final _chatService = ChatService();
  final List<types.Message> _messages = [];
  String? _sessionId;
  final _user = const types.User(id: 'user');
  final _ai = const types.User(id: 'ai', firstName: 'AI');

  @override
  void initState() {
    super.initState();
    _loadInitialSuggestions();
  }

  Future<void> _loadInitialSuggestions() async {
    try {
      final response = await _chatService.getInitialSuggestions();
      setState(() {
        _sessionId = response.sessionId;
        _messages.insert(0, types.TextMessage(
          author: _ai,
          id: DateTime.now().millisecondsSinceEpoch.toString(),
          text: response.reply,
          createdAt: DateTime.now().millisecondsSinceEpoch,
        ));
      });
    } catch (e) {
      // ignore
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: const Text('Chat với AI'),
      ),
      body: Chat(
        messages: _messages,
        onSendPressed: _handleSendPressed,
        user: _user,
        theme: const DefaultChatTheme(
          inputBackgroundColor: Colors.white,
          primaryColor: Color(0xFF0EA5E9),
        ),
      ),
    );
  }

  void _handleSendPressed(types.PartialText message) async {
    final textMessage = types.TextMessage(
      author: _user,
      id: DateTime.now().millisecondsSinceEpoch.toString(),
      text: message.text,
      createdAt: DateTime.now().millisecondsSinceEpoch,
    );

    setState(() {
      _messages.insert(0, textMessage);
    });

    try {
      final response = await _chatService.sendMessage(message.text, _sessionId);

      final aiMessage = types.TextMessage(
        author: _ai,
        id: DateTime.now().millisecondsSinceEpoch.toString(),
        text: response.reply,
        createdAt: DateTime.now().millisecondsSinceEpoch,
      );

      setState(() {
        _messages.insert(0, aiMessage);
        _sessionId = response.sessionId;
      });
    } catch (e) {
      final errorMessage = types.TextMessage(
        author: _ai,
        id: DateTime.now().millisecondsSinceEpoch.toString(),
        text: 'Xin lỗi, tôi đang gặp sự cố. Vui lòng thử lại.',
        createdAt: DateTime.now().millisecondsSinceEpoch,
      );

      setState(() {
        _messages.insert(0, errorMessage);
      });
    }
  }
}
