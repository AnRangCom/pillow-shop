import 'package:flutter/material.dart';
import '../../models/models.dart';
import '../../services/pillow_service.dart';

class PillowListScreen extends StatefulWidget {
  const PillowListScreen({super.key});

  @override
  State<PillowListScreen> createState() => _PillowListScreenState();
}

class _PillowListScreenState extends State<PillowListScreen> {
  final _pillowService = PillowService();
  final _searchController = TextEditingController();
  List<Pillow> _pillows = [];
  bool _loading = true;

  @override
  void initState() {
    super.initState();
    _loadPillows();
  }

  Future<void> _loadPillows({String? search}) async {
    setState(() => _loading = true);
    try {
      final pillows = search != null
          ? await _pillowService.search(search)
          : await _pillowService.getAll();
      setState(() {
        _pillows = pillows;
        _loading = false;
      });
    } catch (e) {
      setState(() => _loading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(title: const Text('Sản phẩm')),
      body: Column(
        children: [
          Padding(
            padding: const EdgeInsets.all(16),
            child: TextField(
              controller: _searchController,
              decoration: InputDecoration(
                hintText: 'Tìm kiếm gối...',
                prefixIcon: const Icon(Icons.search),
                border: OutlineInputBorder(
                  borderRadius: BorderRadius.circular(8),
                ),
              ),
              onSubmitted: (v) => _loadPillows(search: v),
            ),
          ),
          Expanded(
            child: _loading
                ? const Center(child: CircularProgressIndicator())
                : GridView.builder(
                    padding: const EdgeInsets.all(16),
                    gridDelegate: const SliverGridDelegateWithFixedCrossAxisCount(
                      crossAxisCount: 2,
                      childAspectRatio: 0.75,
                      crossAxisSpacing: 16,
                      mainAxisSpacing: 16,
                    ),
                    itemCount: _pillows.length,
                    itemBuilder: (context, index) {
                      return GestureDetector(
                        onTap: () => Navigator.pushNamed(
                          context,
                          '/pillow-detail',
                          arguments: _pillows[index].id,
                        ),
                        child: Card(
                          clipBehavior: Clip.antiAlias,
                          child: Column(
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: [
                              Container(
                                height: 120,
                                color: Colors.grey[200],
                                child: const Center(child: Text('Hình ảnh')),
                              ),
                              Padding(
                                padding: const EdgeInsets.all(8),
                                child: Column(
                                  crossAxisAlignment: CrossAxisAlignment.start,
                                  children: [
                                    Text(
                                      _pillows[index].name,
                                      style: const TextStyle(fontWeight: FontWeight.bold),
                                      maxLines: 2,
                                      overflow: TextOverflow.ellipsis,
                                    ),
                                    const SizedBox(height: 4),
                                    Text(
                                      '${_pillows[index].basePrice.toStringAsFixed(0)} VNĐ',
                                      style: TextStyle(
                                        color: Theme.of(context).primaryColor,
                                        fontWeight: FontWeight.bold,
                                      ),
                                    ),
                                  ],
                                ),
                              ),
                            ],
                          ),
                        ),
                      );
                    },
                  ),
          ),
        ],
      ),
    );
  }
}
