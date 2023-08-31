package ru.bukhtaev.pcassembler.model;

import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.bukhtaev.pcassembler.model.cross.ComputerCaseExpansionBay;
import ru.bukhtaev.pcassembler.model.cross.ComputerCaseFanSize;
import ru.bukhtaev.pcassembler.model.dictionary.MotherboardFormFactor;
import ru.bukhtaev.pcassembler.model.dictionary.PsuFormFactor;
import ru.bukhtaev.pcassembler.model.dictionary.Vendor;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "computer_case",
        uniqueConstraints = @UniqueConstraint(columnNames = "name")
)
@Data
@AllArgsConstructor
@NoArgsConstructor(force = true)
@EqualsAndHashCode(callSuper = true)
public class ComputerCase extends NameableEntity {

    @Min(1)
    @NotNull
    @Column(name = "psu_max_length", nullable = false)
    protected Integer psuMaxLength;

    @Min(1)
    @NotNull
    @Column(name = "graphics_card_max_length", nullable = false)
    protected Integer graphicsCardMaxLength;

    @Min(1)
    @NotNull
    @Column(name = "cooler_max_height", nullable = false)
    protected Integer coolerMaxHeight;

    @ManyToOne
    @JoinColumn(name = "vendor_id", referencedColumnName = "id", nullable = false)
    protected Vendor vendor;

    @Size(min = 1)
    @ManyToMany
    @JoinTable(
            name = "computer_case_to_motherboard_form_factor",
            joinColumns = @JoinColumn(name = "computer_case_id"),
            inverseJoinColumns = @JoinColumn(name = "motherboard_form_factor_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<MotherboardFormFactor> supportedMotherboardFormFactors = new HashSet<>();

    @Size(min = 1)
    @ManyToMany
    @JoinTable(
            name = "computer_case_to_psu_form_factor",
            joinColumns = @JoinColumn(name = "computer_case_id"),
            inverseJoinColumns = @JoinColumn(name = "psu_form_factor_id")
    )
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    protected Set<PsuFormFactor> supportedPsuFormFactors = new HashSet<>();

    @NotNull
    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "computerCase", cascade = CascadeType.ALL)
    protected Set<@Valid @NotNull ComputerCaseExpansionBay> supportedExpansionBays = new HashSet<>();

    @NotNull
    @Size(min = 1)
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "computerCase", cascade = CascadeType.ALL)
    protected Set<@Valid @NotNull ComputerCaseFanSize> supportedFanSizes = new HashSet<>();

    @Builder
    public ComputerCase(
            final String id,
            final Date createdAt,
            final Date modifiedAt,
            final String name,
            final @NotNull Integer psuMaxLength,
            final @NotNull Integer graphicsCardMaxLength,
            final @NotNull Integer coolerMaxHeight,
            final Vendor vendor,
            final Set<MotherboardFormFactor> supportedMotherboardFormFactors,
            final Set<PsuFormFactor> supportedPsuFormFactors,
            final Set<@Valid @NotNull ComputerCaseExpansionBay> supportedExpansionBays,
            final Set<@Valid @NotNull ComputerCaseFanSize> supportedFanSizes
    ) {
        super(id, createdAt, modifiedAt, name);
        this.psuMaxLength = psuMaxLength;
        this.graphicsCardMaxLength = graphicsCardMaxLength;
        this.coolerMaxHeight = coolerMaxHeight;
        this.vendor = vendor;
        this.supportedMotherboardFormFactors = supportedMotherboardFormFactors;
        this.supportedPsuFormFactors = supportedPsuFormFactors;
        this.supportedExpansionBays = supportedExpansionBays;
        this.supportedFanSizes = supportedFanSizes;
    }
}
