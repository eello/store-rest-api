package xyz.fm.storerestapi.entity.user.consumer;

import xyz.fm.storerestapi.entity.shipping.ShippingAddress;
import xyz.fm.storerestapi.entity.user.BaseUserEntity;
import xyz.fm.storerestapi.error.Error;
import xyz.fm.storerestapi.error.ErrorDetail;
import xyz.fm.storerestapi.error.exception.NotFoundException;

import javax.persistence.*;
import java.util.*;

@Entity
@Table(name = "STORE_CONSUMER")
public class Consumer extends BaseUserEntity {

    @Id @GeneratedValue
    @Column(name = "consumer_id")
    private Long id;

    private Long reserves = 0L;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ads_receive_id")
    private AdsReceive adsReceive;

    @OneToMany(mappedBy = "consumer", cascade = CascadeType.ALL)
    private Set<ShippingAddress> shippingAddresses = new HashSet<>();

    protected Consumer() {/* empty */}

    private Consumer(Builder builder) {
        super(builder.email, builder.name, builder.password, builder.phoneNumber);
        this.id = builder.id;
        this.reserves = builder.reserves;
        this.adsReceive = builder.adsReceive;
    }

    public Long getId() {
        return id;
    }

    public Long getReserves() {
        return reserves;
    }

    public AdsReceive getAdsReceive() {
        return adsReceive;
    }

    public Set<ShippingAddress> getShippingAddresses() {
        return shippingAddresses;
    }

    public List<ShippingAddress> getShippingAddressesAsList() {
        return new ArrayList<>(shippingAddresses);
    }

    //== business ==//
    public void addShippingAddress(ShippingAddress shippingAddress) {
        shippingAddress.setConsumer(this);
        this.shippingAddresses.add(shippingAddress);
    }

    public boolean designateDefaultShippingAddress(Long shippingAddressId) {
        ShippingAddress originDefaultShippingAddress = null;

        boolean flag = false;
        for (ShippingAddress shippingAddress : shippingAddresses) {
            if (Objects.equals(shippingAddress.getId(), shippingAddressId)) {
                shippingAddress.setDefaultAddress(true);
                flag = true;
            } else if (shippingAddress.isDefaultAddress()) {
                originDefaultShippingAddress = shippingAddress;
            }
        }

        if (flag && originDefaultShippingAddress != null) {
            originDefaultShippingAddress.setDefaultAddress(false);
            return true;
        } else {
            return false;
        }
    }

    //== builder ==//
    public static class Builder {
        private Long id;
        private final String email;
        private final String name;
        private final String password;
        private final String phoneNumber;
        private final AdsReceive adsReceive;
        private Long reserves = 0L;

        public Builder(String email, String name, String password, String phoneNumber, AdsReceive adsReceive) {
            this.email = email;
            this.name = name;
            this.password = password;
            this.phoneNumber = phoneNumber;
            this.adsReceive = adsReceive;
        }

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder reserves(Long reserves) {
            this.reserves = reserves;
            return this;
        }

        public Consumer build() {
            return new Consumer(this);
        }
    }
}
