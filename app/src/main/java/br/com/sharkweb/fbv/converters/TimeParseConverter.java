package br.com.sharkweb.fbv.converters;

import com.parse.ParseObject;

import br.com.sharkweb.fbv.model.Time;

/**
 * Created by i848340 on 3/22/16.
 */

public class TimeParseConverter extends AbstractConverter<ParseObject,Time> {

    public TimeParseConverter() {
        super(ParseObject.class, Time.class);
    }

    @Override
    public Time convert(ParseObject p) {
        return new Time(p.getString("nome").trim(), p.getString("cidade").trim(), p.getInt("id_uf"), p.getObjectId());
    }
}
