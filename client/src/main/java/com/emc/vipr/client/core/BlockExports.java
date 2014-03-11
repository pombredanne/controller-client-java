package com.emc.vipr.client.core;

import static com.emc.vipr.client.core.util.ResourceUtils.defaultList;
import static com.emc.vipr.client.core.util.ResourceUtils.id;

import java.net.URI;
import java.util.*;

import javax.ws.rs.core.UriBuilder;

import com.emc.storageos.model.BulkIdParam;
import com.emc.storageos.model.block.export.ExportCreateParam;
import com.emc.storageos.model.block.export.ExportGroupBulkRep;
import com.emc.storageos.model.block.export.ExportGroupRestRep;
import com.emc.storageos.model.block.export.ExportUpdateParam;
import com.emc.storageos.model.block.export.ITLRestRep;
import com.emc.storageos.model.block.export.ITLRestRepList;
import com.emc.storageos.model.host.HostRestRep;
import com.emc.storageos.model.host.InitiatorRestRep;
import com.emc.vipr.client.Task;
import com.emc.vipr.client.Tasks;
import com.emc.vipr.client.ViPRCoreClient;
import com.emc.vipr.client.core.filters.*;
import com.emc.vipr.client.core.impl.PathConstants;
import com.emc.vipr.client.impl.RestClient;

/**
 * Block export resources.
 * <p>
 * Base URL: <tt>/block/exports</tt>
 * 
 * @see ExportGroupRestRep
 */
public class BlockExports extends ProjectResources<ExportGroupRestRep> implements TaskResources<ExportGroupRestRep> {
    public BlockExports(ViPRCoreClient parent, RestClient client) {
        super(parent, client, ExportGroupRestRep.class, PathConstants.EXPORT_GROUP_URL);
    }

    @Override
    public BlockExports withInactive(boolean inactive) {
        return (BlockExports) super.withInactive(inactive);
    }

    @Override
    protected List<ExportGroupRestRep> getBulkResources(BulkIdParam input) {
        ExportGroupBulkRep response = client.post(ExportGroupBulkRep.class, input, getBulkUrl());
        return defaultList(response.getExports());
    }

    @Override
    public Tasks<ExportGroupRestRep> getTasks(URI id) {
        return doGetTasks(id);
    }

    @Override
    public Task<ExportGroupRestRep> getTask(URI id, URI taskId) {
        return doGetTask(id, taskId);
    }

    /**
     * Begins creating a block export.
     * <p>
     * API Call: <tt>POST /block/exports</tt>
     * 
     * @param input
     *        the create configuration.
     * @return a task for monitoring the progress of the export creation.
     */
    public Task<ExportGroupRestRep> create(ExportCreateParam input) {
        return postTask(input, baseUrl);
    }

    /**
     * Begins updating a block export.
     * <p>
     * API Call: <tt>PUT /block/exports/{id}</tt>
     * 
     * @param id
     *        the ID of the block export to update.
     * @param input
     *        the update configuration.
     * @return a task for monitoring the progress of the export update.
     */
    public Task<ExportGroupRestRep> update(URI id, ExportUpdateParam input) {
        return putTask(input, getIdUrl(), id);
    }

    /**
     * Begins deactivating a block export by ID.
     * <p>
     * API Call: <tt>POST /block/exports/{id}/deactivate</tt>
     * 
     * @param id
     *        the ID of the block export to deactivate.
     * @return a task for monitoring the progress of the export deactivate.
     */
    public Task<ExportGroupRestRep> deactivate(URI id) {
        return doDeactivateWithTask(id);
    }

    /**
     * Finds the exports associated with a host.
     *
     * @param clusterId
     *        the ID of the cluster.
     * @param projectId
     *        the ID of the project to restrict exports to, or null for no restriction*
     * @param varrayId
     *        the ID of the varray to restrict exports to, or null for no restriction*
     * @return the list of export groups associated with the cluster.
     */
    public List<ExportGroupRestRep> findByCluster(URI clusterId, URI projectId, URI varrayId) {
        List<String> initiators = new ArrayList<String>();
        for (HostRestRep host : parent.hosts().getByCluster(clusterId)) {
            for (InitiatorRestRep initiator : parent.initiators().getByHost(host.getId())) {
                initiators.add(initiator.getInitiatorPort());
            }
        }

        List<URI> blockExportIds = new ArrayList<URI>();
        for (ITLRestRep itl : parent.blockExports().getExportsForInitiatorPorts(initiators)) {
            blockExportIds.add(itl.getExport().getId());
        }

        return parent.blockExports().getByIds(blockExportIds, new ExportClusterFilter(clusterId, projectId, varrayId));
    }

    /**
     * Finds the exports associated with a host.
     *
     * @param hostId
     *        the ID of the host.
     * @param projectId
     *        the ID of the project to restrict exports to, or null for no restriction
     * @param varrayId
     *        the ID of the varray to restrict exports to, or null for no restriction*     *
     * @return the list of export groups associated with the host.
     *
     */
    public List<ExportGroupRestRep> findByHost(URI hostId, URI projectId, URI varrayId) {
        List<String> initiators = new ArrayList<String>();
        for (InitiatorRestRep initiator : parent.initiators().getByHost(hostId)) {
            initiators.add(initiator.getInitiatorPort());
        }

        List<URI> blockExportIds = new ArrayList<URI>();
        for (ITLRestRep itl : parent.blockExports().getExportsForInitiatorPorts(initiators)) {
            blockExportIds.add(itl.getExport().getId());
        }

        return parent.blockExports().getByIds(blockExportIds, new ExportHostFilter(hostId, projectId, varrayId));
    }

    /**
     * Finds any exports (HOST, CLUSTER or EXCLUSIVE) that contain the host.
     *
     * @param hostId
     *        the ID of the host.
     * @param projectId
     *        the ID of the project to restrict exports to, or null for no restriction
     * @param varrayId
     *        the ID of the varray to restrict exports to, or null for no restriction     *
     * @return the list of export groups associated with the host.
     */
    public List<ExportGroupRestRep> findContainingHost(URI hostId, URI projectId, URI varrayId) {
        List<String> initiators = new ArrayList<String>();
        for (InitiatorRestRep initiator : parent.initiators().getByHost(hostId)) {
            initiators.add(initiator.getInitiatorPort());
        }

        List<URI> blockExportIds = new ArrayList<URI>();
        for (ITLRestRep itl : parent.blockExports().getExportsForInitiatorPorts(initiators)) {
            blockExportIds.add(itl.getExport().getId());
        }

        return parent.blockExports().getByIds(blockExportIds, new ProjectFilter(projectId).and(new VirtualArrayFilter(varrayId)));
    }


    /**
     * Finds the exports associated with a host (or that host's cluster) that are for the given project. If a virtual
     * array ID is specified, only exports associated with that virtual array are returned.
     * 
     * @param hostId
     *        the ID of the host.
     * @param projectId
     *        the ID of the project.
     * @param virtualArrayId
     *        the ID of the virtual array to restrict the exports to, or null for no restriction.
     * @return the list of export groups associated with the host or any host in the same cluster.
     */
    public List<ExportGroupRestRep> findByHostOrCluster(URI hostId, URI projectId, URI virtualArrayId) {
        HostRestRep host = parent.hosts().get(hostId);
        URI clusterId = (host != null) ? id(host.getCluster()) : null;

        ResourceFilter<ExportGroupRestRep> filter;
        if (virtualArrayId == null) {
            filter = new ExportHostOrClusterFilter(hostId, clusterId);
        }
        else {
            filter = new ExportHostOrClusterFilter(hostId, clusterId).and(new ExportVirtualArrayFilter(virtualArrayId));
        }

        return findByProject(projectId, filter);
    }

    /**
     * Finds the exports associated with a host (or that host's cluster) that are for the given project.
     * 
     * @param hostId
     *        the ID of the host.
     * @param projectId
     *        the ID of the project.
     * @return the list of export groups associated with the host or any host in the same cluster.
     */
    public List<ExportGroupRestRep> findByHostOrCluster(URI hostId, URI projectId) {
        return findByHostOrCluster(hostId, projectId, null);
    }

    /**
     * Gets the exports (initiator-target-lun) for the given initiators. This is a convenience method that collects the
     * provided initiators' ports and calls {@link #getExportsForInitiatorPorts(Collection)}.
     * 
     * @param initiators
     *        the initiators.
     * @return the list of exports.
     */
    public List<ITLRestRep> getExportsForInitiators(Collection<InitiatorRestRep> initiators) {
        Set<String> ports = new LinkedHashSet<String>();
        for (InitiatorRestRep initiator : initiators) {
            if (initiator.getInitiatorPort() != null && initiator.getInitiatorPort().length() > 0) {
                ports.add(initiator.getInitiatorPort());
            }
        }
        return getExportsForInitiatorPorts(ports);
    }

    /**
     * Gets the exports (initiator-target-lun) for the given initiator ports.
     * <p>
     * API Call: <tt>GET /block/exports?initiators={initiatorPort1},{initiatorPort2},...</tt>
     * 
     * @param initiatorPorts
     *        the initiator ports.
     * @return the list of exports.
     */
    public List<ITLRestRep> getExportsForInitiatorPorts(Collection<String> initiatorPorts) {
        UriBuilder builder = client.uriBuilder(baseUrl);
        StringBuilder ports = new StringBuilder();
        for (String initiatorPort : initiatorPorts) {
            if (ports.length() > 0) {
                ports.append(',');
            }
            ports.append(initiatorPort);
        }
        builder.queryParam("initiators", ports.toString());
        ITLRestRepList list = client.getURI(ITLRestRepList.class, builder.build());
        return defaultList(list.getExportList());
    }
}
