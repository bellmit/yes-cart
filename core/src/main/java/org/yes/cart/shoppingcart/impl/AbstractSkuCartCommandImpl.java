package org.yes.cart.shoppingcart.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.yes.cart.constants.ServiceSpringKeys;
import org.yes.cart.shoppingcart.CartItem;
import org.yes.cart.domain.dto.ProductSkuDTO;
import org.yes.cart.shoppingcart.ShoppingCart;
import org.yes.cart.domain.entity.Product;
import org.yes.cart.domain.entity.Shop;
import org.yes.cart.service.domain.PriceService;
import org.yes.cart.service.domain.ProductService;
import org.yes.cart.service.domain.ShopService;
import org.yes.cart.service.dto.DtoProductService;
import org.yes.cart.shoppingcart.ShoppingCartCommand;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Map;

/**
 * Abstract sku cart command.
 * <p/>
 * User: Igor Azarny iazarny@yahoo.com
 * Date: 09-May-2011
 * Time: 14:12:54
 */
public abstract class AbstractSkuCartCommandImpl extends AbstractCartCommandImpl implements ShoppingCartCommand {

    private static final long serialVersionUID = 20100313L;

    private static final Logger LOG = LoggerFactory.getLogger(AbstractSkuCartCommandImpl.class);

    private ProductSkuDTO productSkuDTO;

    private PriceService priceService;

    private ProductService productService;

    private DtoProductService dtoProductService;

    private ShopService shopService;


    /**
     * Construct abstracr sku command.
     *
     * @param applicationContext spring context
     * @param parameters         parameters
     */
    public AbstractSkuCartCommandImpl(final ApplicationContext applicationContext, final Map parameters) {
        super();
        String skuCode = (String) parameters.get(getCmdKey());
        try {
            shopService = (ShopService) applicationContext.getBean(ServiceSpringKeys.SHOP_SERVICE);
            productService = (ProductService) applicationContext.getBean(ServiceSpringKeys.PRODUCT_SERVICE);
            priceService = (PriceService) applicationContext.getBean(ServiceSpringKeys.PRICE_SERVICE);
            dtoProductService = (DtoProductService) applicationContext.getBean(ServiceSpringKeys.DTO_PRODUCT_SERVICE);
            productSkuDTO = dtoProductService.getProductSkuByCode(skuCode);
        } catch (Exception e) {
            LOG.error(MessageFormat.format("Can not retreive product sku dto with code {0}", skuCode), e);
        }

    }

    /**
     * Recalucalate price in shopping cart. At this moment price depends from shop, currenct and quantity.
     * Promotions also can impact the price. It will be implemented letter
     *
     * @param shoppingCart shopping cart
     */
    protected void recalculatePrice(final ShoppingCart shoppingCart) {

        if (shoppingCart.getShoppingContext().getShopId() == 0) {

            LOG.error("Can not recalculate price because the shop id is 0");

        } else {

            final Shop shop = shopService.getById(shoppingCart.getShoppingContext().getShopId());

            if (getProductSkuDTO() == null) {

                for (int i = 0; i < shoppingCart.getCartItemList().size(); i++) {

                    final CartItem cartItem = shoppingCart.getCartItemList().get(i);

                    setProductSkuPrice(shoppingCart, shop, cartItem.getProductSkuCode(), cartItem.getQty());

                }

            } else {
                // particular sku command
                final String skuCode = getProductSkuDTO().getCode();

                int skuIdx = shoppingCart.indexOf(skuCode);

                if (skuIdx != -1) {

                    setProductSkuPrice(shoppingCart, shop, skuCode, shoppingCart.getCartItemList().get(skuIdx).getQty());

                }
            }
        }

    }

    private void setProductSkuPrice(final ShoppingCart shoppingCart,
                                    final Shop shop,
                                    final String skuCode,
                                    final BigDecimal qty) {

        final Product product = getProductService().getProductBySkuCode(skuCode);

        final BigDecimal price = getPriceService().getMinimalPrice(
                product.getSku(),
                skuCode,
                shop,
                shoppingCart.getCurrencyCode(),
                qty
        );

        if (!shoppingCart.setProductSkuPrice(skuCode, price)) {
            LOG.warn(MessageFormat.format("Can not set price to sku with code {0} ",
                    skuCode));

        }
    }


    /**
     * Get price service.
     *
     * @return {@link PriceService}
     */
    public PriceService getPriceService() {
        return priceService;
    }

    /**
     * Get {@link DtoProductService}.
     *
     * @return {@link DtoProductService}.
     */
    public DtoProductService getDtoProductService() {
        return dtoProductService;
    }

    /**
     * Get sku dto.
     *
     * @return optional {@link ProductSkuDTO} that related to current command.
     */
    public ProductSkuDTO getProductSkuDTO() {
        return productSkuDTO;
    }

    /**
     * Get product Service.
     *
     * @return product service.
     */
    public ProductService getProductService() {
        return productService;
    }
}