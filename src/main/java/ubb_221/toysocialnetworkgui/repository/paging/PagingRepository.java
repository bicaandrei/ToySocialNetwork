package ubb_221.toysocialnetworkgui.repository.paging;


import ubb_221.toysocialnetworkgui.domain.Entity;
import ubb_221.toysocialnetworkgui.repository.RepositoryInterface;

public interface PagingRepository<ID, E extends Entity<ID>> extends RepositoryInterface<ID, E> {
    Page<E> findAll(Pageable pageable);   // Pageable e un fel de paginator

}
