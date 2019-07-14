package com.cherkashin.vitaliy.gui.main.result.commodity_list;

import java.text.DecimalFormat;


import java.util.Iterator;
import java.util.Date;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.behavior.AbstractBehavior;
import org.apache.wicket.behavior.SimpleAttributeModifier;
import org.apache.wicket.extensions.markup.html.repeater.data.grid.ICellPopulator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.ISortStateLocator;
import org.apache.wicket.extensions.markup.html.repeater.data.sort.OrderByBorder;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.AbstractToolbar;
import org.apache.wicket.extensions.markup.html.repeater.data.table.DataTable;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.IStyledColumn;
import org.apache.wicket.extensions.markup.html.repeater.data.table.NavigationToolbar;
import org.apache.wicket.extensions.markup.html.repeater.util.SortableDataProvider;
import org.apache.wicket.markup.ComponentTag;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.panel.EmptyPanel;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.markup.repeater.Item;
import org.apache.wicket.markup.repeater.RepeatingView;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.request.target.basic.RedirectRequestTarget;
import org.apache.wicket.util.string.Strings;

import com.cherkashin.vitaliy.dao.Find;
import com.cherkashin.vitaliy.dao.SearchingResult;

import database.ConnectWrap;
import database.StaticConnector;

import wicket_utility.mapper_pattern.IInterceptor;
import wicket_utility.mapper_pattern.Mapper;
import wicket_utility.mapper_pattern.MapperInterceptor;
import wicket_utility.mapper_pattern.MapperMessage;
import wicket_utility.panel_redirect_link.PanelRedirectLink;

/** панель верхнего колонтитура */
public class ResultPanel extends Panel implements IInterceptor{
	private final static long serialVersionUID=1L;
	@SuppressWarnings("unused")
	private Mapper parentMapper;
	private Mapper currentMapper;
	private final String mapperId="current";
	private final String mapperOptionNameRedirect="redirectToPosition";
	private final String mapperOptionShopNameRedirect="redirectToShop";
	private final Model<String> modelDataEmpty=new Model<String>(this.getString("empty_data"));
	/** панель верхнего колонтитура  
	 * @param id - имя панели 
	 * @param mapper - объект-шаблон Mapper
	 */
	public ResultPanel(String idPanelResult, 
					   Mapper mapper,
					   String findString, 
					   int recordPerPage, 
					   int pageNumber){
		super(idPanelResult);
		this.parentMapper=mapper;
		this.currentMapper=new Mapper();
		this.currentMapper.addInterceptor(new MapperInterceptor(this));
		initComponents(findString, recordPerPage, pageNumber);
	}

	private DecimalFormat decimalFormat=new DecimalFormat("#.00");
	private ResultDataProvider dataProvider=new ResultDataProvider();
	
	
	
	/** первоначальная инициализация компонентов */
	@SuppressWarnings("unchecked")
	private void initComponents(String findString, int recordPerPage, int pageNumber){
		IColumn[] columns={
				new AbstractColumn<SearchingResult>(new Model<String>(this.getString("title_shop_name"))){
					private final static long serialVersionUID=1L;
					@Override
					public void populateItem( Item<ICellPopulator<SearchingResult>> item, String markupId, IModel<SearchingResult> model) {
						PanelRedirectLink link=new PanelRedirectLink(markupId, 
																	 model.getObject().getShopName(), 
																	 currentMapper, 
																	 mapperId, 
																	 mapperOptionShopNameRedirect, 
																	 model.getObject().getUniqueRecordId());
						item.add(link);
					}
					@Override
					public String getCssClass() {
						return "c_1";
					}
				},
				new AbstractColumn<SearchingResult>(new Model<String>(this.getString("title_name"))){
					private final static long serialVersionUID=1L;
					@Override
					public void populateItem( Item<ICellPopulator<SearchingResult>> item, String markupId, IModel<SearchingResult> model) {
						PanelRedirectLink link=new PanelRedirectLink(markupId, 
																	 model.getObject().getName(), 
																	 currentMapper, 
																	 mapperId, 
																	 mapperOptionNameRedirect, 
																	 model.getObject().getUniqueRecordId());
						item.add(link);
					}
					@Override
					public String getCssClass() {
						return "c_2";
					}
				},
				new AbstractColumn<SearchingResult>(new Model<String>(this.getString("title_price"))){
					private final static long serialVersionUID=1L;
					@Override
					public void populateItem( Item<ICellPopulator<SearchingResult>> item, String markupId, IModel<SearchingResult> model) {
						Label label=null;
						SearchingResult object=model.getObject();
						if(object==null){
							item.add(new EmptyPanel(markupId));
						}else{
							if(model.getObject().getPrice()==0){
								if(model.getObject().getCalculatedHrn()!=0){
									label=new Label(markupId, decimalFormat.format(model.getObject().getCalculatedHrn()));
									label.add(new SimpleAttributeModifier("class", "calc_hrn"));
								}else{
									label=new Label(markupId);
								}
							}else{
								label=new Label(markupId, decimalFormat.format(model.getObject().getPrice()));
							}
							item.add(label);
						}
					}
					@Override
					public String getCssClass() {
						return "c_3";
					}
				},
				new AbstractColumn<SearchingResult>(new Model<String>(this.getString("title_price_usd"))){
					private final static long serialVersionUID=1L;
					@Override
					public void populateItem( Item<ICellPopulator<SearchingResult>> item, String markupId, IModel<SearchingResult> model) {
						Label label=null;
						SearchingResult object=model.getObject();
						if(object==null){
							item.add(new EmptyPanel(markupId));
						}else{
							if(model.getObject().getPriceUsd()==0){
								if(model.getObject().getCalculatedUsd()!=0){
									label=new Label(markupId, decimalFormat.format(model.getObject().getCalculatedUsd()));
									label.add(new SimpleAttributeModifier("class", "calc_usd"));
								}else{
									label=new Label(markupId);
								}
							}else{
								label=new Label(markupId, decimalFormat.format(model.getObject().getPriceUsd()));
							}
							item.add(label);
						}
					}
					@Override
					public String getCssClass() {
						return "c_4";
					}
				},
				
				
		};
		dataProvider.setSearchString(findString);
		DataTable<SearchingResult> table=new DataTable<SearchingResult>("result_table", columns, dataProvider, 15){
			private final static long serialVersionUID=1L;
			@Override
			protected Item<SearchingResult> newRowItem(String id, int index, IModel<SearchingResult> model) {
				// return new OddEvenItem<SearchingResult>(id, index, model);
				return new Item<SearchingResult>(id,index, model){
						private final static long serialVersionUID=1L;
						@Override
						protected void onComponentTag(ComponentTag tag) {
							super.onComponentTag(tag);
							if(this.getIndex()%2==0){
								tag.put("class", "even_table_result");
							}else{
								tag.put("class", "odd_table_result");
							}
							
						}
				};
			}
		};
		table.addTopToolbar(new NavigationToolbar(table));
		table.addTopToolbar(new HeadersToolbar(table, dataProvider));
		table.addBottomToolbar(new NoRecordsToolbar(table, modelDataEmpty));

		this.add(table);
	}



	@Override
	public String getInterceptorId() {
		return mapperId;
	}



	@Override
	public void getOutsideMessage(MapperMessage message) {
		Find find=new Find();
		if(message.getOptionName()!=null){
			// System.out.println("OptionName: "+message.getOptionName());
			if(message.getOptionName().equals(mapperOptionNameRedirect)){
				// INFO redirect to Position
				// System.out.println("Redirect to position:"+message.getParameters());
				String urlToPosition=this.getUrlToPosition(find, (Integer)message.getParameters());
				System.out.println(urlToPosition);
				this.getRequestCycle().setRequestTarget(new RedirectRequestTarget(urlToPosition));
				return ;
			}
			if(message.getOptionName().equals(mapperOptionShopNameRedirect)){
				// INFO redirect to Shop
				// System.out.println("Redirect to shop:"+message.getParameters());
				String urlToShop=this.getUrlToShop(find, (Integer)message.getParameters());
				System.out.println(urlToShop);
				this.getRequestCycle().setRequestTarget(new RedirectRequestTarget(urlToShop));
				return ;
			}
		}
		// setResponsePage(new RedirectPage("http://google.com.ua"));
	}
	
	private String getUrlToShop(Find find, Integer value){
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			return find.getShopUrl(connector, value);
		}finally{
			connector.close();
		}
	}
	
	private String getUrlToPosition(Find find, Integer value){
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			return find.getPositionUrl(connector, value);
		}finally{
			connector.close();
		}
	}
	
}

class NoRecordsToolbar extends AbstractToolbar
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param table
	 *            data table this toolbar will be attached to
	 */
	public NoRecordsToolbar(final DataTable<?> table, Model<String> emptyMessage)
	{
		this(table, (IModel<String>)emptyMessage);
	}

	/**
	 * @param table
	 *            data table this toolbar will be attached to
	 * @param messageModel
	 *            model that will be used to display the "no records found" message
	 */
	public NoRecordsToolbar(final DataTable<?> table, IModel<String> messageModel)
	{
		super(table);
		WebMarkupContainer td = new WebMarkupContainer("td");
		add(td);

		td.add(new AttributeModifier("colspan", true, new Model<String>(String.valueOf(table.getColumns().length))));
		td.add(new Label("msg", messageModel));
	}

	/**
	 * Only shows this toolbar when there are no rows
	 * 
	 * @see org.apache.wicket.Component#isVisible()
	 */
	@Override
	public boolean isVisible()
	{
		return getTable().getRowCount() == 0;
	}

}


/**
 * Toolbars that displays column headers. If the column is sortable a sortable header will be
 * displayed.
 * 
 * @see DefaultDataTable
 * 
 * @author Igor Vaynberg (ivaynberg)
 * 
 */
class HeadersToolbar extends AbstractToolbar
{
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor
	 * 
	 * @param table
	 *            data table this toolbar will be attached to
	 * @param stateLocator
	 *            locator for the ISortState implementation used by sortable headers
	 */
	@SuppressWarnings("unchecked")
	public HeadersToolbar(final DataTable<?> table, final ISortStateLocator stateLocator)
	{
		super(table);

		RepeatingView headers = new RepeatingView("headers");
		add(headers);

		final IColumn<?>[] columns = table.getColumns();
		for (final IColumn<?> column : columns)
		{
			WebMarkupContainer item = new WebMarkupContainer(headers.newChildId());
			headers.add(item);

			WebMarkupContainer header = null;
			if (column.isSortable())
			{
				header = newSortableHeader("header", column.getSortProperty(), stateLocator);
			}
			else
			{
				header = new WebMarkupContainer("header");
			}

			if (column instanceof IStyledColumn)
			{
				header.add(new CssAttributeBehavior()
				{
					private static final long serialVersionUID = 1L;

					@Override
					protected String getCssClass()
					{
						return ((IStyledColumn<?>)column).getCssClass();
					}
				});
			}

			item.add(header);
			item.setRenderBodyOnly(true);
			header.add(column.getHeader("label"));

		}
	}

	/**
	 * Factory method for sortable header components. A sortable header component must have id of
	 * <code>headerId</code> and conform to markup specified in <code>HeadersToolbar.html</code>
	 * 
	 * @param headerId
	 *            header component id
	 * @param property
	 *            property this header represents
	 * @param locator
	 *            sort state locator
	 * @return created header component
	 */
	protected WebMarkupContainer newSortableHeader(String headerId, String property,ISortStateLocator locator)
	{
		return new OrderByBorder(headerId, property, locator)
		{
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSortChanged()
			{
				getTable().setCurrentPage(0);
			}
		};
	}

	@Override
	public boolean isVisible() {
		return this.getTable().getRowCount()>0;
	}
}

abstract class CssAttributeBehavior extends AbstractBehavior
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected abstract String getCssClass();

	/**
	 * @see IBehavior#onComponentTag(Component, ComponentTag)
	 */
	@Override
	public void onComponentTag(Component component, ComponentTag tag)
	{
		String className = getCssClass();
		if (!Strings.isEmpty(className))
		{
			CharSequence oldClassName = tag.getString("class");
			if (Strings.isEmpty(oldClassName))
			{
				tag.put("class", className);
			}
			else
			{
				tag.put("class", oldClassName + " " + className);
			}
		}
	}
}



class ResultDataProvider extends SortableDataProvider<SearchingResult>{
	private final static long serialVersionUID=1L;
	private String searchingString=null;
	
	/** установить строку поиска для данных  */
	public void setSearchString(String searchingString){
		this.searchingString=searchingString;
	}
	
	@Override
	public Iterator<? extends SearchingResult> iterator(int first, int count) {
		Find find=new Find();
		ConnectWrap connector=StaticConnector.getConnectWrap();
		try{
			return find.findCommodity(connector, this.searchingString, first, count, find.getCurrentCourse(connector, new Date())).iterator();
		}finally{
			connector.close();
		}
	}

	@Override
	public IModel<SearchingResult> model(SearchingResult result) {
		return new Model<SearchingResult>(result);
	}

	@Override
	public int size() {
		Find find=new Find();
		if(this.searchingString==null){
			return 0;
		}else{
			ConnectWrap connector=StaticConnector.getConnectWrap();
			try{
				return find.findCommodityGetCount(connector, this.searchingString);
			}finally{
				connector.close();
			}
		}
	}
}

/*
		ListView<SearchingResult> listView=new ListView<SearchingResult>("result_list", list){
			private final static long serialVersionUID=1L;

			@Override
			protected void populateItem(ListItem<SearchingResult> element) {
				SearchingResult searchingResult=element.getModelObject();
				element.add(new Label("result_shop_name", searchingResult.getShopName()));
				element.add(new Label("result_name", searchingResult.getName()));
				if(searchingResult.getPrice()==0){
					element.add(new Label("result_price", decimalFormat.format(searchingResult.getCalculatedPriceByUsd())));
				}else{
					element.add(new Label("result_price", decimalFormat.format(searchingResult.getPrice())));
				}
				element.add(new Label("result_price_usd", decimalFormat.format(searchingResult.getPriceUsd())));
			}
		};

		<table width="100%">
			<tr>
				<th width="150px">
					<wicket:message key="title_shop_name" />
				</th>
				<th width="450px">
					<wicket:message key="title_name" />
				</th>
				<th width="50px">
					<wicket:message key="title_price" />
				</th>
				<th width="50px">
					<wicket:message key="title_price_usd"></div>
				</th>
			</tr>
			<tr wicket:id="result_list" >	
				<td width="150px">
					<div wicket:id="result_shop_name"></div>
				</td>
				<td width="450px">
					<div wicket:id="result_name"></div>
				</td>
				<td width="50px" align="right">
					<div wicket:id="result_price"></div>
				</td>
				<td width="50px" align="right">
					<div wicket:id="result_price_usd"></div>
				</td>
			</tr>
		</table>
*/