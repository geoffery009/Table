package one.hundred.table.item.lib

import one.hundred.table.item.*

/**
 * Created by zzy on 2017/10/13.
 */
object ItemTransformationFactory {
    /**
     * 格式化数据为ItemViewModel
     * @param needObserver 参数当不为空时，通知观察者更新数据
     * @param map 缓存编辑框，及选择框的数据
     */
    @Synchronized
    fun transformationViewData(list: List<ItemTableBean>,
                               map: MutableMap<String, Any>,
                               itemEditObserver: ((String, String) -> Unit)? = null,
                               itemSelectObserver: ((String, String) -> Unit)? = null,
                               itemTextClick: ((ItemText) -> Unit)? = null,
                               itemSelectClick: ((ItemSelect) -> Unit)? = null,
                               actionClick: ((ItemAction) -> Unit)? = null): List<ItemTable> =
            list.map {
                when (it.type) {
                    is ItemTableType.Text -> {
                        ItemText(it, {
                            itemTextClick?.invoke(it)
                        })
                    }
                    is ItemTableType.TextEdit -> {
                        ItemEdit(it, { key, value ->
                            if ((it.type as ItemTableType.TextEdit).isEnable)
                                map.put(key, value)
                            itemEditObserver?.invoke(key, value)
                        })
                    }
                    is ItemTableType.TextSelect -> {
                        ItemSelect(it, { key, value ->
                            map.put(key, value)
                            itemSelectObserver?.invoke(key, value)
                        }, {
                            itemSelectClick?.invoke(it)
                        })
                    }
                    is ItemTableType.Divider -> {
                        ItemDivider(it)
                    }
                    is ItemTableType.TextAction -> {
                        ItemAction(it, {
                            actionClick?.invoke(it)
                        })
                    }
                }
            }
}