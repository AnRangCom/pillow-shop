import 'package:flutter/material.dart';
import '../../models/models.dart';
import '../../services/pillow_service.dart';

class PillowDetailScreen extends StatefulWidget {
  final int pillowId;

  const PillowDetailScreen({super.key, required this.pillowId});

  @override
  State<PillowDetailScreen> createState() => _PillowDetailScreenState();
}

class _PillowDetailScreenState extends State<PillowDetailScreen> {
  final _pillowService = PillowService();
  Pillow? _pillow;
  List<DefaultSize> _sizes = [];
  int? _selectedSizeId;
  int _quantity = 1;
  bool _loading = true;

  @override
  void initState() {
    super.initState();
    _loadData();
  }

  Future<void> _loadData() async {
    try {
      final pillow = await _pillowService.getById(widget.pillowId);
      final sizes = await _pillowService.getSizes(widget.pillowId);
      setState(() {
        _pillow = pillow;
        _sizes = sizes;
        _loading = false;
      });
    } catch (e) {
      setState(() => _loading = false);
    }
  }

  @override
  Widget build(BuildContext context) {
    if (_loading) {
      return Scaffold(
        appBar: AppBar(),
        body: const Center(child: CircularProgressIndicator()),
      );
    }

    if (_pillow == null) {
      return Scaffold(
        appBar: AppBar(),
        body: const Center(child: Text('Không tìm thấy sản phẩm')),
      );
    }

    return Scaffold(
      appBar: AppBar(title: Text(_pillow!.name)),
      body: SingleChildScrollView(
        child: Column(
          crossAxisAlignment: CrossAxisAlignment.start,
          children: [
            Container(
              height: 250,
              width: double.infinity,
              color: Colors.grey[200],
              child: const Center(child: Text('Hình ảnh sản phẩm')),
            ),
            Padding(
              padding: const EdgeInsets.all(16),
              child: Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                children: [
                  Text(
                    _pillow!.name,
                    style: const TextStyle(fontSize: 24, fontWeight: FontWeight.bold),
                  ),
                  const SizedBox(height: 8),
                  Text(
                    '${_pillow!.basePrice.toStringAsFixed(0)} VNĐ',
                    style: TextStyle(
                      fontSize: 20,
                      color: Theme.of(context).primaryColor,
                      fontWeight: FontWeight.bold,
                    ),
                  ),
                  if (_pillow!.material != null) ...[
                    const SizedBox(height: 8),
                    Text('Chất liệu: ${_pillow!.material}'),
                  ],
                  if (_pillow!.description != null) ...[
                    const SizedBox(height: 16),
                    Text(_pillow!.description!),
                  ],
                  if (_sizes.isNotEmpty) ...[
                    const SizedBox(height: 16),
                    const Text(
                      'Kích thước:',
                      style: TextStyle(fontWeight: FontWeight.bold),
                    ),
                    const SizedBox(height: 8),
                    Wrap(
                      spacing: 8,
                      children: _sizes.map((size) {
                        return ChoiceChip(
                          label: Text('${size.name} (${size.length}x${size.width})'),
                          selected: _selectedSizeId == size.id,
                          onSelected: (selected) {
                            setState(() => _selectedSizeId = selected ? size.id : null);
                          },
                        );
                      }).toList(),
                    ),
                  ],
                  const SizedBox(height: 16),
                  Row(
                    children: [
                      const Text('Số lượng:', style: TextStyle(fontWeight: FontWeight.bold)),
                      const SizedBox(width: 16),
                      IconButton(
                        icon: const Icon(Icons.remove),
                        onPressed: () => setState(() {
                          if (_quantity > 1) _quantity--;
                        }),
                      ),
                      Text('$_quantity', style: const TextStyle(fontSize: 18)),
                      IconButton(
                        icon: const Icon(Icons.add),
                        onPressed: () => setState(() => _quantity++),
                      ),
                    ],
                  ),
                  const SizedBox(height: 24),
                  SizedBox(
                    width: double.infinity,
                    height: 48,
                    child: ElevatedButton(
                      onPressed: _addToCart,
                      child: const Text('Thêm vào giỏ hàng'),
                    ),
                  ),
                ],
              ),
            ),
          ],
        ),
      ),
    );
  }

  void _addToCart() {
    ScaffoldMessenger.of(context).showSnackBar(
      const SnackBar(content: Text('Đã thêm vào giỏ hàng!')),
    );
  }
}
