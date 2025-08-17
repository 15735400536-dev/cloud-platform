-- 1. 仓库表
CREATE TABLE warehouse (
                           id VARCHAR(32) PRIMARY KEY,
                           warehouse_code VARCHAR(50) NOT NULL UNIQUE,
                           warehouse_name VARCHAR(100) NOT NULL,
                           address VARCHAR(255),
                           contact_person VARCHAR(50),
                           contact_phone VARCHAR(20),
                           status SMALLINT NOT NULL DEFAULT 1,
                           remark VARCHAR(500),
                           create_by VARCHAR(32),
                           update_by VARCHAR(32),
                           del_flag INT2 NOT NULL DEFAULT 0,
                           create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           update_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE warehouse IS '仓库信息表';
COMMENT ON COLUMN warehouse.id IS '仓库ID';
COMMENT ON COLUMN warehouse.warehouse_code IS '仓库编码';
COMMENT ON COLUMN warehouse.warehouse_name IS '仓库名称';
COMMENT ON COLUMN warehouse.address IS '仓库地址';
COMMENT ON COLUMN warehouse.contact_person IS '联系人';
COMMENT ON COLUMN warehouse.contact_phone IS '联系电话';
COMMENT ON COLUMN warehouse.status IS '状态：0-禁用，1-启用';
COMMENT ON COLUMN warehouse.remark IS '备注';
COMMENT ON COLUMN warehouse.create_by IS '创建人';
COMMENT ON COLUMN warehouse.update_by IS '更新人';
COMMENT ON COLUMN warehouse.del_flag IS '删除标识：0-未删除，1-已删除';
COMMENT ON COLUMN warehouse.create_time IS '创建时间';
COMMENT ON COLUMN warehouse.update_time IS '更新时间';
CREATE INDEX idx_warehouse_code ON warehouse(warehouse_code);


-- 2. 库区表
CREATE TABLE warehouse_zone (
                                id VARCHAR(32) PRIMARY KEY,
                                warehouse_id VARCHAR(32) NOT NULL,
                                zone_code VARCHAR(50) NOT NULL,
                                zone_name VARCHAR(100) NOT NULL,
                                status SMALLINT NOT NULL DEFAULT 1,
                                remark VARCHAR(500),
                                create_by VARCHAR(32),
                                update_by VARCHAR(32),
                                del_flag INT2 NOT NULL DEFAULT 0,
                                create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                update_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                CONSTRAINT fk_warehouse_zone_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse(id),
                                CONSTRAINT uk_zone_code UNIQUE (warehouse_id, zone_code)
);
COMMENT ON TABLE warehouse_zone IS '仓库库区表';
COMMENT ON COLUMN warehouse_zone.id IS '库区ID';
COMMENT ON COLUMN warehouse_zone.warehouse_id IS '仓库ID';
COMMENT ON COLUMN warehouse_zone.zone_code IS '库区编码';
COMMENT ON COLUMN warehouse_zone.zone_name IS '库区名称';
COMMENT ON COLUMN warehouse_zone.status IS '状态：0-禁用，1-启用';
COMMENT ON COLUMN warehouse_zone.remark IS '备注';
COMMENT ON COLUMN warehouse_zone.create_by IS '创建人';
COMMENT ON COLUMN warehouse_zone.update_by IS '更新人';
COMMENT ON COLUMN warehouse_zone.del_flag IS '删除标识：0-未删除，1-已删除';
COMMENT ON COLUMN warehouse_zone.create_time IS '创建时间';
COMMENT ON COLUMN warehouse_zone.update_time IS '更新时间';
CREATE INDEX idx_warehouse_id ON warehouse_zone(warehouse_id);


-- 3. 货架表
CREATE TABLE warehouse_rack (
                                id VARCHAR(32) PRIMARY KEY,
                                warehouse_id VARCHAR(32) NOT NULL,
                                zone_id VARCHAR(32) NOT NULL,
                                rack_code VARCHAR(50) NOT NULL,
                                rack_name VARCHAR(100),
                                status SMALLINT NOT NULL DEFAULT 1,
                                remark VARCHAR(500),
                                create_by VARCHAR(32),
                                update_by VARCHAR(32),
                                del_flag INT2 NOT NULL DEFAULT 0,
                                create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                update_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                CONSTRAINT fk_warehouse_rack_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse(id),
                                CONSTRAINT fk_warehouse_rack_zone FOREIGN KEY (zone_id) REFERENCES warehouse_zone(id),
                                CONSTRAINT uk_rack_code UNIQUE (warehouse_id, rack_code)
);
COMMENT ON TABLE warehouse_rack IS '仓库货架表';
COMMENT ON COLUMN warehouse_rack.id IS '货架ID';
COMMENT ON COLUMN warehouse_rack.warehouse_id IS '仓库ID';
COMMENT ON COLUMN warehouse_rack.zone_id IS '库区ID';
COMMENT ON COLUMN warehouse_rack.rack_code IS '货架编码';
COMMENT ON COLUMN warehouse_rack.rack_name IS '货架名称';
COMMENT ON COLUMN warehouse_rack.status IS '状态：0-禁用，1-启用';
COMMENT ON COLUMN warehouse_rack.remark IS '备注';
COMMENT ON COLUMN warehouse_rack.create_by IS '创建人';
COMMENT ON COLUMN warehouse_rack.update_by IS '更新人';
COMMENT ON COLUMN warehouse_rack.del_flag IS '删除标识：0-未删除，1-已删除';
COMMENT ON COLUMN warehouse_rack.create_time IS '创建时间';
COMMENT ON COLUMN warehouse_rack.update_time IS '更新时间';
CREATE INDEX idx_zone_id ON warehouse_rack(zone_id);


-- 4. 货位表
CREATE TABLE warehouse_location (
                                    id VARCHAR(32) PRIMARY KEY,
                                    warehouse_id VARCHAR(32) NOT NULL,
                                    zone_id VARCHAR(32) NOT NULL,
                                    rack_id VARCHAR(32) NOT NULL,
                                    location_code VARCHAR(50) NOT NULL,
                                    location_name VARCHAR(100),
                                    location_type SMALLINT,
                                    status SMALLINT NOT NULL DEFAULT 1,
                                    remark VARCHAR(500),
                                    create_by VARCHAR(32),
                                    update_by VARCHAR(32),
                                    del_flag INT2 NOT NULL DEFAULT 0,
                                    create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    update_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    CONSTRAINT fk_warehouse_location_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse(id),
                                    CONSTRAINT fk_warehouse_location_zone FOREIGN KEY (zone_id) REFERENCES warehouse_zone(id),
                                    CONSTRAINT fk_warehouse_location_rack FOREIGN KEY (rack_id) REFERENCES warehouse_rack(id),
                                    CONSTRAINT uk_location_code UNIQUE (warehouse_id, location_code)
);
COMMENT ON TABLE warehouse_location IS '仓库货位表';
COMMENT ON COLUMN warehouse_location.id IS '货位ID';
COMMENT ON COLUMN warehouse_location.warehouse_id IS '仓库ID';
COMMENT ON COLUMN warehouse_location.zone_id IS '库区ID';
COMMENT ON COLUMN warehouse_location.rack_id IS '货架ID';
COMMENT ON COLUMN warehouse_location.location_code IS '货位编码';
COMMENT ON COLUMN warehouse_location.location_name IS '货位名称';
COMMENT ON COLUMN warehouse_location.location_type IS '货位类型：1-普通，2-冷藏，3-冷冻等';
COMMENT ON COLUMN warehouse_location.status IS '状态：0-禁用，1-启用，2-占用';
COMMENT ON COLUMN warehouse_location.remark IS '备注';
COMMENT ON COLUMN warehouse_location.create_by IS '创建人';
COMMENT ON COLUMN warehouse_location.update_by IS '更新人';
COMMENT ON COLUMN warehouse_location.del_flag IS '删除标识：0-未删除，1-已删除';
COMMENT ON COLUMN warehouse_location.create_time IS '创建时间';
COMMENT ON COLUMN warehouse_location.update_time IS '更新时间';
CREATE INDEX idx_rack_id ON warehouse_location(rack_id);


-- 5. 物料/商品表
CREATE TABLE material (
                          id VARCHAR(32) PRIMARY KEY,
                          material_code VARCHAR(50) NOT NULL UNIQUE,
                          material_name VARCHAR(200) NOT NULL,
                          specification VARCHAR(200),
                          unit VARCHAR(20),
                          category_id VARCHAR(32),
                          barcode VARCHAR(100),
                          safety_stock NUMERIC(18,6) DEFAULT 0,
                          status SMALLINT NOT NULL DEFAULT 1,
                          remark VARCHAR(500),
                          create_by VARCHAR(32),
                          update_by VARCHAR(32),
                          del_flag INT2 NOT NULL DEFAULT 0,
                          create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                          update_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP
);
COMMENT ON TABLE material IS '物料/商品信息表';
COMMENT ON COLUMN material.id IS '物料ID';
COMMENT ON COLUMN material.material_code IS '物料编码';
COMMENT ON COLUMN material.material_name IS '物料名称';
COMMENT ON COLUMN material.specification IS '规格型号';
COMMENT ON COLUMN material.unit IS '单位';
COMMENT ON COLUMN material.category_id IS '物料分类ID';
COMMENT ON COLUMN material.barcode IS '条形码';
COMMENT ON COLUMN material.safety_stock IS '安全库存';
COMMENT ON COLUMN material.status IS '状态：0-禁用，1-启用';
COMMENT ON COLUMN material.remark IS '备注';
COMMENT ON COLUMN material.create_by IS '创建人';
COMMENT ON COLUMN material.update_by IS '更新人';
COMMENT ON COLUMN material.del_flag IS '删除标识：0-未删除，1-已删除';
COMMENT ON COLUMN material.create_time IS '创建时间';
COMMENT ON COLUMN material.update_time IS '更新时间';
CREATE INDEX idx_material_code ON material(material_code);
CREATE INDEX idx_barcode ON material(barcode);


-- 6. 库存表
CREATE TABLE inventory (
                           id VARCHAR(32) PRIMARY KEY,
                           warehouse_id VARCHAR(32) NOT NULL,
                           location_id VARCHAR(32) NOT NULL,
                           material_id VARCHAR(32) NOT NULL,
                           batch_no VARCHAR(50),
                           quantity NUMERIC(18,6) NOT NULL DEFAULT 0,
                           locked_quantity NUMERIC(18,6) NOT NULL DEFAULT 0,
                           available_quantity NUMERIC(18,6) NOT NULL DEFAULT 0,
                           production_date DATE,
                           expiry_date DATE,
                           last_stocktaking_time TIMESTAMPTZ,
                           remark VARCHAR(500),
                           create_by VARCHAR(32),
                           update_by VARCHAR(32),
                           del_flag INT2 NOT NULL DEFAULT 0,
                           create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           update_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                           CONSTRAINT fk_inventory_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse(id),
                           CONSTRAINT fk_inventory_location FOREIGN KEY (location_id) REFERENCES warehouse_location(id),
                           CONSTRAINT fk_inventory_material FOREIGN KEY (material_id) REFERENCES material(id),
                           CONSTRAINT uk_inventory UNIQUE (warehouse_id, location_id, material_id, batch_no)
);
COMMENT ON TABLE inventory IS '库存表';
COMMENT ON COLUMN inventory.id IS '库存ID';
COMMENT ON COLUMN inventory.warehouse_id IS '仓库ID';
COMMENT ON COLUMN inventory.location_id IS '货位ID';
COMMENT ON COLUMN inventory.material_id IS '物料ID';
COMMENT ON COLUMN inventory.batch_no IS '批次号';
COMMENT ON COLUMN inventory.quantity IS '库存数量';
COMMENT ON COLUMN inventory.locked_quantity IS '锁定数量';
COMMENT ON COLUMN inventory.available_quantity IS '可用数量';
COMMENT ON COLUMN inventory.production_date IS '生产日期';
COMMENT ON COLUMN inventory.expiry_date IS '过期日期';
COMMENT ON COLUMN inventory.last_stocktaking_time IS '最后盘点时间';
COMMENT ON COLUMN inventory.remark IS '备注';
COMMENT ON COLUMN inventory.create_by IS '创建人';
COMMENT ON COLUMN inventory.update_by IS '更新人';
COMMENT ON COLUMN inventory.del_flag IS '删除标识：0-未删除，1-已删除';
COMMENT ON COLUMN inventory.create_time IS '创建时间';
COMMENT ON COLUMN inventory.update_time IS '更新时间';
CREATE INDEX idx_material_id ON inventory(material_id);


-- 7. 入库单表
CREATE TABLE receipt_order (
                               id VARCHAR(32) PRIMARY KEY,
                               order_no VARCHAR(50) NOT NULL UNIQUE,
                               warehouse_id VARCHAR(32) NOT NULL,
                               order_type SMALLINT NOT NULL,
                               source_order_no VARCHAR(50),
                               status SMALLINT NOT NULL,
                               receipt_time TIMESTAMPTZ,
                               total_quantity NUMERIC(18,6) DEFAULT 0,
                               total_amount NUMERIC(18,2) DEFAULT 0,
                               operator VARCHAR(50),
                               remark VARCHAR(500),
                               create_by VARCHAR(32),
                               update_by VARCHAR(32),
                               del_flag INT2 NOT NULL DEFAULT 0,
                               create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               update_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                               CONSTRAINT fk_receipt_order_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse(id)
);
COMMENT ON TABLE receipt_order IS '入库单表';
COMMENT ON COLUMN receipt_order.id IS '入库单ID';
COMMENT ON COLUMN receipt_order.order_no IS '入库单号';
COMMENT ON COLUMN receipt_order.warehouse_id IS '仓库ID';
COMMENT ON COLUMN receipt_order.order_type IS '入库类型：1-采购入库，2-生产入库，3-退货入库，4-调拨入库等';
COMMENT ON COLUMN receipt_order.source_order_no IS '源单号（如采购单号）';
COMMENT ON COLUMN receipt_order.status IS '状态：0-草稿，1-待审核，2-已审核，3-已完成，4-已取消';
COMMENT ON COLUMN receipt_order.receipt_time IS '入库时间';
COMMENT ON COLUMN receipt_order.total_quantity IS '总数量';
COMMENT ON COLUMN receipt_order.total_amount IS '总金额';
COMMENT ON COLUMN receipt_order.operator IS '操作员';
COMMENT ON COLUMN receipt_order.remark IS '备注';
COMMENT ON COLUMN receipt_order.create_by IS '创建人';
COMMENT ON COLUMN receipt_order.update_by IS '更新人';
COMMENT ON COLUMN receipt_order.del_flag IS '删除标识：0-未删除，1-已删除';
COMMENT ON COLUMN receipt_order.create_time IS '创建时间';
COMMENT ON COLUMN receipt_order.update_time IS '更新时间';
CREATE INDEX idx_order_no ON receipt_order(order_no);
CREATE INDEX idx_receipt_warehouse_id ON receipt_order(warehouse_id);
CREATE INDEX idx_receipt_status ON receipt_order(status);


-- 8. 入库单明细表
CREATE TABLE receipt_order_item (
                                    id VARCHAR(32) PRIMARY KEY,
                                    receipt_order_id VARCHAR(32) NOT NULL,
                                    material_id VARCHAR(32) NOT NULL,
                                    location_id VARCHAR(32),
                                    batch_no VARCHAR(50),
                                    plan_quantity NUMERIC(18,6) NOT NULL,
                                    actual_quantity NUMERIC(18,6) DEFAULT 0,
                                    unit_cost NUMERIC(18,2),
                                    amount NUMERIC(18,2) DEFAULT 0,
                                    production_date DATE,
                                    expiry_date DATE,
                                    remark VARCHAR(500),
                                    create_by VARCHAR(32),
                                    update_by VARCHAR(32),
                                    del_flag INT2 NOT NULL DEFAULT 0,
                                    create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    update_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                    CONSTRAINT fk_receipt_item_order FOREIGN KEY (receipt_order_id) REFERENCES receipt_order(id),
                                    CONSTRAINT fk_receipt_item_material FOREIGN KEY (material_id) REFERENCES material(id),
                                    CONSTRAINT fk_receipt_item_location FOREIGN KEY (location_id) REFERENCES warehouse_location(id)
);
COMMENT ON TABLE receipt_order_item IS '入库单明细表';
COMMENT ON COLUMN receipt_order_item.id IS '明细ID';
COMMENT ON COLUMN receipt_order_item.receipt_order_id IS '入库单ID';
COMMENT ON COLUMN receipt_order_item.material_id IS '物料ID';
COMMENT ON COLUMN receipt_order_item.location_id IS '货位ID';
COMMENT ON COLUMN receipt_order_item.batch_no IS '批次号';
COMMENT ON COLUMN receipt_order_item.plan_quantity IS '计划数量';
COMMENT ON COLUMN receipt_order_item.actual_quantity IS '实际数量';
COMMENT ON COLUMN receipt_order_item.unit_cost IS '单位成本';
COMMENT ON COLUMN receipt_order_item.amount IS '金额';
COMMENT ON COLUMN receipt_order_item.production_date IS '生产日期';
COMMENT ON COLUMN receipt_order_item.expiry_date IS '过期日期';
COMMENT ON COLUMN receipt_order_item.remark IS '备注';
COMMENT ON COLUMN receipt_order_item.create_by IS '创建人';
COMMENT ON COLUMN receipt_order_item.update_by IS '更新人';
COMMENT ON COLUMN receipt_order_item.del_flag IS '删除标识：0-未删除，1-已删除';
COMMENT ON COLUMN receipt_order_item.create_time IS '创建时间';
COMMENT ON COLUMN receipt_order_item.update_time IS '更新时间';
CREATE INDEX idx_receipt_order_id ON receipt_order_item(receipt_order_id);
CREATE INDEX idx_receipt_item_material_id ON receipt_order_item(material_id);


-- 9. 出库单表
CREATE TABLE issue_order (
                             id VARCHAR(32) PRIMARY KEY,
                             order_no VARCHAR(50) NOT NULL UNIQUE,
                             warehouse_id VARCHAR(32) NOT NULL,
                             order_type SMALLINT NOT NULL,
                             source_order_no VARCHAR(50),
                             status SMALLINT NOT NULL,
                             issue_time TIMESTAMPTZ,
                             total_quantity NUMERIC(18,6) DEFAULT 0,
                             total_amount NUMERIC(18,2) DEFAULT 0,
                             operator VARCHAR(50),
                             remark VARCHAR(500),
                             create_by VARCHAR(32),
                             update_by VARCHAR(32),
                             del_flag INT2 NOT NULL DEFAULT 0,
                             create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             update_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             CONSTRAINT fk_issue_order_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse(id)
);
COMMENT ON TABLE issue_order IS '出库单表';
COMMENT ON COLUMN issue_order.id IS '出库单ID';
COMMENT ON COLUMN issue_order.order_no IS '出库单号';
COMMENT ON COLUMN issue_order.warehouse_id IS '仓库ID';
COMMENT ON COLUMN issue_order.order_type IS '出库类型：1-销售出库，2-生产领料，3-调拨出库，4-报废出库等';
COMMENT ON COLUMN issue_order.source_order_no IS '源单号（如销售单号）';
COMMENT ON COLUMN issue_order.status IS '状态：0-草稿，1-待审核，2-已审核，3-已完成，4-已取消';
COMMENT ON COLUMN issue_order.issue_time IS '出库时间';
COMMENT ON COLUMN issue_order.total_quantity IS '总数量';
COMMENT ON COLUMN issue_order.total_amount IS '总金额';
COMMENT ON COLUMN issue_order.operator IS '操作员';
COMMENT ON COLUMN issue_order.remark IS '备注';
COMMENT ON COLUMN issue_order.create_by IS '创建人';
COMMENT ON COLUMN issue_order.update_by IS '更新人';
COMMENT ON COLUMN issue_order.del_flag IS '删除标识：0-未删除，1-已删除';
COMMENT ON COLUMN issue_order.create_time IS '创建时间';
COMMENT ON COLUMN issue_order.update_time IS '更新时间';
CREATE INDEX idx_issue_order_no ON issue_order(order_no);
CREATE INDEX idx_issue_warehouse_id ON issue_order(warehouse_id);
CREATE INDEX idx_issue_status ON issue_order(status);


-- 10. 出库单明细表
CREATE TABLE issue_order_item (
                                  id VARCHAR(32) PRIMARY KEY,
                                  issue_order_id VARCHAR(32) NOT NULL,
                                  material_id VARCHAR(32) NOT NULL,
                                  location_id VARCHAR(32),
                                  batch_no VARCHAR(50),
                                  plan_quantity NUMERIC(18,6) NOT NULL,
                                  actual_quantity NUMERIC(18,6) DEFAULT 0,
                                  unit_cost NUMERIC(18,2),
                                  amount NUMERIC(18,2) DEFAULT 0,
                                  remark VARCHAR(500),
                                  create_by VARCHAR(32),
                                  update_by VARCHAR(32),
                                  del_flag INT2 NOT NULL DEFAULT 0,
                                  create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  update_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  CONSTRAINT fk_issue_item_order FOREIGN KEY (issue_order_id) REFERENCES issue_order(id),
                                  CONSTRAINT fk_issue_item_material FOREIGN KEY (material_id) REFERENCES material(id),
                                  CONSTRAINT fk_issue_item_location FOREIGN KEY (location_id) REFERENCES warehouse_location(id)
);
COMMENT ON TABLE issue_order_item IS '出库单明细表';
COMMENT ON COLUMN issue_order_item.id IS '明细ID';
COMMENT ON COLUMN issue_order_item.issue_order_id IS '出库单ID';
COMMENT ON COLUMN issue_order_item.material_id IS '物料ID';
COMMENT ON COLUMN issue_order_item.location_id IS '货位ID';
COMMENT ON COLUMN issue_order_item.batch_no IS '批次号';
COMMENT ON COLUMN issue_order_item.plan_quantity IS '计划数量';
COMMENT ON COLUMN issue_order_item.actual_quantity IS '实际数量';
COMMENT ON COLUMN issue_order_item.unit_cost IS '单位成本';
COMMENT ON COLUMN issue_order_item.amount IS '金额';
COMMENT ON COLUMN issue_order_item.remark IS '备注';
COMMENT ON COLUMN issue_order_item.create_by IS '创建人';
COMMENT ON COLUMN issue_order_item.update_by IS '更新人';
COMMENT ON COLUMN issue_order_item.del_flag IS '删除标识：0-未删除，1-已删除';
COMMENT ON COLUMN issue_order_item.create_time IS '创建时间';
COMMENT ON COLUMN issue_order_item.update_time IS '更新时间';
CREATE INDEX idx_issue_order_id ON issue_order_item(issue_order_id);
CREATE INDEX idx_issue_item_material_id ON issue_order_item(material_id);


-- 11. 库存调整单表
CREATE TABLE inventory_adjustment (
                                      id VARCHAR(32) PRIMARY KEY,
                                      adjustment_no VARCHAR(50) NOT NULL UNIQUE,
                                      warehouse_id VARCHAR(32) NOT NULL,
                                      adjustment_type SMALLINT NOT NULL,
                                      status SMALLINT NOT NULL,
                                      adjustment_time TIMESTAMPTZ,
                                      total_quantity NUMERIC(18,6) DEFAULT 0,
                                      operator VARCHAR(50),
                                      remark VARCHAR(500),
                                      create_by VARCHAR(32),
                                      update_by VARCHAR(32),
                                      del_flag INT2 NOT NULL DEFAULT 0,
                                      create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      update_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                      CONSTRAINT fk_adjustment_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse(id)
);
COMMENT ON TABLE inventory_adjustment IS '库存调整单表';
COMMENT ON COLUMN inventory_adjustment.id IS '调整单ID';
COMMENT ON COLUMN inventory_adjustment.adjustment_no IS '调整单号';
COMMENT ON COLUMN inventory_adjustment.warehouse_id IS '仓库ID';
COMMENT ON COLUMN inventory_adjustment.adjustment_type IS '调整类型：1-盘盈，2-盘亏，3-其他调整';
COMMENT ON COLUMN inventory_adjustment.status IS '状态：0-草稿，1-待审核，2-已审核，3-已完成，4-已取消';
COMMENT ON COLUMN inventory_adjustment.adjustment_time IS '调整时间';
COMMENT ON COLUMN inventory_adjustment.total_quantity IS '调整总数量';
COMMENT ON COLUMN inventory_adjustment.operator IS '操作员';
COMMENT ON COLUMN inventory_adjustment.remark IS '备注';
COMMENT ON COLUMN inventory_adjustment.create_by IS '创建人';
COMMENT ON COLUMN inventory_adjustment.update_by IS '更新人';
COMMENT ON COLUMN inventory_adjustment.del_flag IS '删除标识：0-未删除，1-已删除';
COMMENT ON COLUMN inventory_adjustment.create_time IS '创建时间';
COMMENT ON COLUMN inventory_adjustment.update_time IS '更新时间';
CREATE INDEX idx_adjustment_no ON inventory_adjustment(adjustment_no);
CREATE INDEX idx_adjustment_warehouse_id ON inventory_adjustment(warehouse_id);


-- 12. 库存调整单明细表
CREATE TABLE inventory_adjustment_item (
                                           id VARCHAR(32) PRIMARY KEY,
                                           adjustment_id VARCHAR(32) NOT NULL,
                                           material_id VARCHAR(32) NOT NULL,
                                           location_id VARCHAR(32) NOT NULL,
                                           batch_no VARCHAR(50),
                                           before_quantity NUMERIC(18,6) NOT NULL,
                                           adjustment_quantity NUMERIC(18,6) NOT NULL,
                                           after_quantity NUMERIC(18,6) NOT NULL,
                                           reason VARCHAR(500),
                                           remark VARCHAR(500),
                                           create_by VARCHAR(32),
                                           update_by VARCHAR(32),
                                           del_flag INT2 NOT NULL DEFAULT 0,
                                           create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           update_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                           CONSTRAINT fk_adjustment_item_main FOREIGN KEY (adjustment_id) REFERENCES inventory_adjustment(id),
                                           CONSTRAINT fk_adjustment_item_material FOREIGN KEY (material_id) REFERENCES material(id),
                                           CONSTRAINT fk_adjustment_item_location FOREIGN KEY (location_id) REFERENCES warehouse_location(id)
);
COMMENT ON TABLE inventory_adjustment_item IS '库存调整单明细表';
COMMENT ON COLUMN inventory_adjustment_item.id IS '明细ID';
COMMENT ON COLUMN inventory_adjustment_item.adjustment_id IS '调整单ID';
COMMENT ON COLUMN inventory_adjustment_item.material_id IS '物料ID';
COMMENT ON COLUMN inventory_adjustment_item.location_id IS '货位ID';
COMMENT ON COLUMN inventory_adjustment_item.batch_no IS '批次号';
COMMENT ON COLUMN inventory_adjustment_item.before_quantity IS '调整前数量';
COMMENT ON COLUMN inventory_adjustment_item.adjustment_quantity IS '调整数量（正数为增加，负数为减少）';
COMMENT ON COLUMN inventory_adjustment_item.after_quantity IS '调整后数量';
COMMENT ON COLUMN inventory_adjustment_item.reason IS '调整原因';
COMMENT ON COLUMN inventory_adjustment_item.remark IS '备注';
COMMENT ON COLUMN inventory_adjustment_item.create_by IS '创建人';
COMMENT ON COLUMN inventory_adjustment_item.update_by IS '更新人';
COMMENT ON COLUMN inventory_adjustment_item.del_flag IS '删除标识：0-未删除，1-已删除';
COMMENT ON COLUMN inventory_adjustment_item.create_time IS '创建时间';
COMMENT ON COLUMN inventory_adjustment_item.update_time IS '更新时间';
CREATE INDEX idx_adjustment_id ON inventory_adjustment_item(adjustment_id);
CREATE INDEX idx_adjustment_item_material_id ON inventory_adjustment_item(material_id);


-- 13. 盘点单表
CREATE TABLE stocktaking (
                             id VARCHAR(32) PRIMARY KEY,
                             stocktaking_no VARCHAR(50) NOT NULL UNIQUE,
                             warehouse_id VARCHAR(32) NOT NULL,
                             zone_id VARCHAR(32),
                             status SMALLINT NOT NULL,
                             start_time TIMESTAMPTZ,
                             end_time TIMESTAMPTZ,
                             operator VARCHAR(50),
                             remark VARCHAR(500),
                             create_by VARCHAR(32),
                             update_by VARCHAR(32),
                             del_flag INT2 NOT NULL DEFAULT 0,
                             create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             update_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                             CONSTRAINT fk_stocktaking_warehouse FOREIGN KEY (warehouse_id) REFERENCES warehouse(id),
                             CONSTRAINT fk_stocktaking_zone FOREIGN KEY (zone_id) REFERENCES warehouse_zone(id)
);
COMMENT ON TABLE stocktaking IS '盘点单表';
COMMENT ON COLUMN stocktaking.id IS '盘点单ID';
COMMENT ON COLUMN stocktaking.stocktaking_no IS '盘点单号';
COMMENT ON COLUMN stocktaking.warehouse_id IS '仓库ID';
COMMENT ON COLUMN stocktaking.zone_id IS '库区ID（为空则盘点整个仓库）';
COMMENT ON COLUMN stocktaking.status IS '状态：0-草稿，1-进行中，2-已完成，3-已取消';
COMMENT ON COLUMN stocktaking.start_time IS '开始时间';
COMMENT ON COLUMN stocktaking.end_time IS '结束时间';
COMMENT ON COLUMN stocktaking.operator IS '操作员';
COMMENT ON COLUMN stocktaking.remark IS '备注';
COMMENT ON COLUMN stocktaking.create_by IS '创建人';
COMMENT ON COLUMN stocktaking.update_by IS '更新人';
COMMENT ON COLUMN stocktaking.del_flag IS '删除标识：0-未删除，1-已删除';
COMMENT ON COLUMN stocktaking.create_time IS '创建时间';
COMMENT ON COLUMN stocktaking.update_time IS '更新时间';
CREATE INDEX idx_stocktaking_no ON stocktaking(stocktaking_no);
CREATE INDEX idx_stocktaking_warehouse_id ON stocktaking(warehouse_id);


-- 14. 盘点单明细表
CREATE TABLE stocktaking_item (
                                  id VARCHAR(32) PRIMARY KEY,
                                  stocktaking_id VARCHAR(32) NOT NULL,
                                  material_id VARCHAR(32) NOT NULL,
                                  location_id VARCHAR(32) NOT NULL,
                                  batch_no VARCHAR(50),
                                  system_quantity NUMERIC(18,6) NOT NULL,
                                  actual_quantity NUMERIC(18,6),
                                  difference_quantity NUMERIC(18,6),
                                  remark VARCHAR(500),
                                  create_by VARCHAR(32),
                                  update_by VARCHAR(32),
                                  del_flag INT2 NOT NULL DEFAULT 0,
                                  create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  update_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                  CONSTRAINT fk_stocktaking_item_main FOREIGN KEY (stocktaking_id) REFERENCES stocktaking(id),
                                  CONSTRAINT fk_stocktaking_item_material FOREIGN KEY (material_id) REFERENCES material(id),
                                  CONSTRAINT fk_stocktaking_item_location FOREIGN KEY (location_id) REFERENCES warehouse_location(id)
);
COMMENT ON TABLE stocktaking_item IS '盘点单明细表';
COMMENT ON COLUMN stocktaking_item.id IS '明细ID';
COMMENT ON COLUMN stocktaking_item.stocktaking_id IS '盘点单ID';
COMMENT ON COLUMN stocktaking_item.material_id IS '物料ID';
COMMENT ON COLUMN stocktaking_item.location_id IS '货位ID';
COMMENT ON COLUMN stocktaking_item.batch_no IS '批次号';
COMMENT ON COLUMN stocktaking_item.system_quantity IS '系统库存数量';
COMMENT ON COLUMN stocktaking_item.actual_quantity IS '实际盘点数量';
COMMENT ON COLUMN stocktaking_item.difference_quantity IS '差异数量';
COMMENT ON COLUMN stocktaking_item.remark IS '备注';
COMMENT ON COLUMN stocktaking_item.create_by IS '创建人';
COMMENT ON COLUMN stocktaking_item.update_by IS '更新人';
COMMENT ON COLUMN stocktaking_item.del_flag IS '删除标识：0-未删除，1-已删除';
COMMENT ON COLUMN stocktaking_item.create_time IS '创建时间';
COMMENT ON COLUMN stocktaking_item.update_time IS '更新时间';
CREATE INDEX idx_stocktaking_id ON stocktaking_item(stocktaking_id);
CREATE INDEX idx_stocktaking_item_material_id ON stocktaking_item(material_id);


-- 15. 移库单表
CREATE TABLE transfer_order (
                                id VARCHAR(32) PRIMARY KEY,
                                transfer_no VARCHAR(50) NOT NULL UNIQUE,
                                source_warehouse_id VARCHAR(32) NOT NULL,
                                target_warehouse_id VARCHAR(32) NOT NULL,
                                status SMALLINT NOT NULL,
                                transfer_time TIMESTAMPTZ,
                                total_quantity NUMERIC(18,6) DEFAULT 0,
                                operator VARCHAR(50),
                                remark VARCHAR(500),
                                create_by VARCHAR(32),
                                update_by VARCHAR(32),
                                del_flag INT2 NOT NULL DEFAULT 0,
                                create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                update_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                CONSTRAINT fk_transfer_source_warehouse FOREIGN KEY (source_warehouse_id) REFERENCES warehouse(id),
                                CONSTRAINT fk_transfer_target_warehouse FOREIGN KEY (target_warehouse_id) REFERENCES warehouse(id)
);
COMMENT ON TABLE transfer_order IS '移库单表';
COMMENT ON COLUMN transfer_order.id IS '移库单ID';
COMMENT ON COLUMN transfer_order.transfer_no IS '移库单号';
COMMENT ON COLUMN transfer_order.source_warehouse_id IS '源仓库ID';
COMMENT ON COLUMN transfer_order.target_warehouse_id IS '目标仓库ID';
COMMENT ON COLUMN transfer_order.status IS '状态：0-草稿，1-待审核，2-已审核，3-已完成，4-已取消';
COMMENT ON COLUMN transfer_order.transfer_time IS '移库时间';
COMMENT ON COLUMN transfer_order.total_quantity IS '总数量';
COMMENT ON COLUMN transfer_order.operator IS '操作员';
COMMENT ON COLUMN transfer_order.remark IS '备注';
COMMENT ON COLUMN transfer_order.create_by IS '创建人';
COMMENT ON COLUMN transfer_order.update_by IS '更新人';
COMMENT ON COLUMN transfer_order.del_flag IS '删除标识：0-未删除，1-已删除';
COMMENT ON COLUMN transfer_order.create_time IS '创建时间';
COMMENT ON COLUMN transfer_order.update_time IS '更新时间';
CREATE INDEX idx_transfer_no ON transfer_order(transfer_no);
CREATE INDEX idx_source_warehouse ON transfer_order(source_warehouse_id);
CREATE INDEX idx_target_warehouse ON transfer_order(target_warehouse_id);


-- 16. 移库单明细表
CREATE TABLE transfer_order_item (
                                     id VARCHAR(32) PRIMARY KEY,
                                     transfer_order_id VARCHAR(32) NOT NULL,
                                     material_id VARCHAR(32) NOT NULL,
                                     source_location_id VARCHAR(32),
                                     target_location_id VARCHAR(32),
                                     batch_no VARCHAR(50),
                                     plan_quantity NUMERIC(18,6) NOT NULL,
                                     actual_quantity NUMERIC(18,6) DEFAULT 0,
                                     remark VARCHAR(500),
                                     create_by VARCHAR(32),
                                     update_by VARCHAR(32),
                                     del_flag INT2 NOT NULL DEFAULT 0,
                                     create_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     update_time TIMESTAMPTZ NOT NULL DEFAULT CURRENT_TIMESTAMP,
                                     CONSTRAINT fk_transfer_item_main FOREIGN KEY (transfer_order_id) REFERENCES transfer_order(id),
                                     CONSTRAINT fk_transfer_item_material FOREIGN KEY (material_id) REFERENCES material(id),
                                     CONSTRAINT fk_transfer_item_source_location FOREIGN KEY (source_location_id) REFERENCES warehouse_location(id),
                                     CONSTRAINT fk_transfer_item_target_location FOREIGN KEY (target_location_id) REFERENCES warehouse_location(id)
);
COMMENT ON TABLE transfer_order_item IS '移库单明细表';
COMMENT ON COLUMN transfer_order_item.id IS '明细ID';
COMMENT ON COLUMN transfer_order_item.transfer_order_id IS '移库单ID';
COMMENT ON COLUMN transfer_order_item.material_id IS '物料ID';
COMMENT ON COLUMN transfer_order_item.source_location_id IS '源货位ID';
COMMENT ON COLUMN transfer_order_item.target_location_id IS '目标货位ID';
COMMENT ON COLUMN transfer_order_item.batch_no IS '批次号';
COMMENT ON COLUMN transfer_order_item.plan_quantity IS '计划数量';
COMMENT ON COLUMN transfer_order_item.actual_quantity IS '实际数量';
COMMENT ON COLUMN transfer_order_item.remark IS '备注';
COMMENT ON COLUMN transfer_order_item.create_by IS '创建人';
COMMENT ON COLUMN transfer_order_item.update_by IS '更新人';
COMMENT ON COLUMN transfer_order_item.del_flag IS '删除标识：0-未删除，1-已删除';
COMMENT ON COLUMN transfer_order_item.create_time IS '创建时间';
COMMENT ON COLUMN transfer_order_item.update_time IS '更新时间';
CREATE INDEX idx_transfer_order_id ON transfer_order_item(transfer_order_id);
CREATE INDEX idx_transfer_item_material_id ON transfer_order_item(material_id);


-- 自动更新update_time和update_by的触发器函数
CREATE OR REPLACE FUNCTION update_modified_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.update_time = CURRENT_TIMESTAMP;
RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 为所有表添加更新时间触发器
CREATE TRIGGER update_warehouse_modtime BEFORE UPDATE ON warehouse FOR EACH ROW EXECUTE FUNCTION update_modified_column();
CREATE TRIGGER update_warehouse_zone_modtime BEFORE UPDATE ON warehouse_zone FOR EACH ROW EXECUTE FUNCTION update_modified_column();
CREATE TRIGGER update_warehouse_rack_modtime BEFORE UPDATE ON warehouse_rack FOR EACH ROW EXECUTE FUNCTION update_modified_column();
CREATE TRIGGER update_warehouse_location_modtime BEFORE UPDATE ON warehouse_location FOR EACH ROW EXECUTE FUNCTION update_modified_column();
CREATE TRIGGER update_material_modtime BEFORE UPDATE ON material FOR EACH ROW EXECUTE FUNCTION update_modified_column();
CREATE TRIGGER update_inventory_modtime BEFORE UPDATE ON inventory FOR EACH ROW EXECUTE FUNCTION update_modified_column();
CREATE TRIGGER update_receipt_order_modtime BEFORE UPDATE ON receipt_order FOR EACH ROW EXECUTE FUNCTION update_modified_column();
CREATE TRIGGER update_receipt_order_item_modtime BEFORE UPDATE ON receipt_order_item FOR EACH ROW EXECUTE FUNCTION update_modified_column();
CREATE TRIGGER update_issue_order_modtime BEFORE UPDATE ON issue_order FOR EACH ROW EXECUTE FUNCTION update_modified_column();
CREATE TRIGGER update_issue_order_item_modtime BEFORE UPDATE ON issue_order_item FOR EACH ROW EXECUTE FUNCTION update_modified_column();
CREATE TRIGGER update_inventory_adjustment_modtime BEFORE UPDATE ON inventory_adjustment FOR EACH ROW EXECUTE FUNCTION update_modified_column();
CREATE TRIGGER update_inventory_adjustment_item_modtime BEFORE UPDATE ON inventory_adjustment_item FOR EACH ROW EXECUTE FUNCTION update_modified_column();
CREATE TRIGGER update_stocktaking_modtime BEFORE UPDATE ON stocktaking FOR EACH ROW EXECUTE FUNCTION update_modified_column();
CREATE TRIGGER update_stocktaking_item_modtime BEFORE UPDATE ON stocktaking_item FOR EACH ROW EXECUTE FUNCTION update_modified_column();
CREATE TRIGGER update_transfer_order_modtime BEFORE UPDATE ON transfer_order FOR EACH ROW EXECUTE FUNCTION update_modified_column();
CREATE TRIGGER update_transfer_order_item_modtime BEFORE UPDATE ON transfer_order_item FOR EACH ROW EXECUTE FUNCTION update_modified_column();