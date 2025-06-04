package com.github.saeidjafarzadeh.hibernatlegacy.criteria;

import org.hibernate.dialect.OracleDialect;
import org.hibernate.dialect.pagination.AbstractLimitHandler;
import org.hibernate.dialect.pagination.LimitHandler;
import org.hibernate.query.spi.Limit;

public class Oracle10gDialect extends OracleDialect {
    public Oracle10gDialect() {
        super();
    }

    private static final AbstractLimitHandler LIMIT_HANDLER = new AbstractLimitHandler() {
        @Override
        public String processSql(String sql, Limit limit) {
            StringBuilder pagingSelect;
            if (limit.getFirstRow()>0 && limit.getMaxRows()>0) {
                pagingSelect = new StringBuilder(sql.length());
                pagingSelect.append("select * from (select row_.*,rownum rownum_ from (");
                pagingSelect.append(sql);
                pagingSelect.append(") row_ where rownum<=?) where rownum_>?");
            } else if (limit.getFirstRow()>0) {
                pagingSelect = new StringBuilder(sql.length() );
                pagingSelect.append("select * from (");
                pagingSelect.append(sql);
                pagingSelect.append(") row_ where rownum>?");
            } else {
                pagingSelect = new StringBuilder(sql.length() );
                pagingSelect.append("select * from (");
                pagingSelect.append(sql);
                pagingSelect.append(") where rownum<=?");
            }

            return pagingSelect.toString();
        }


        @Override
        public boolean supportsLimit() {
            return true;
        }

        @Override
        public boolean useMaxForLimit() {
            return true;
        }

        @Override
        public boolean supportsVariableLimit() {
            return false;
        }
    };
    @Override
    public LimitHandler getLimitHandler() {
        return LIMIT_HANDLER;
    }
}
